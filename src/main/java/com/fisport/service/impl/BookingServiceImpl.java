package com.fisport.service.impl;

import com.fisport.common.EBookingStatus;
import com.fisport.common.EFieldServiceItem;
import com.fisport.common.ESubFieldStatus;
import com.fisport.dto.request.BookingRequest;
import com.fisport.dto.request.BookingServiceItemRequest;
import com.fisport.dto.response.*;
import com.fisport.exception.BookingException;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.*;
import com.fisport.repository.*;
import com.fisport.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SubFieldRepository subFieldRepository;
    private final FieldServiceItemRepository fieldServiceItemRepository;
    private final FieldHasTimeSlotRepository fieldHasTimeSlotRepository;
    private final UserRepository userRepository;
    private final FieldHasTimeSlotService fieldHasTimeSlotService;
    private final FieldTypeBookDurationRepository fieldTypeBookDurationRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherService voucherService;
    private final ChallengeMatchService challengeMatchService;
    private final ChallengeMatchTypeService challengeMatchTypeService;
    private final int SLOT_INTERVAL = 30;

    @Override
    public List<BookingTimeResponse> getOccupiedSlots(Long subFieldId, LocalDate date) {
        List<Booking> bookings = bookingRepository.findOccupiedSlots(subFieldId, date, List.of(EBookingStatus.PENDING, EBookingStatus.PAID)
        );
        return bookings.stream().map(b -> BookingTimeResponse.builder()
                .startTime(b.getStartTime())
                .endTime(b.getEndTime())
                .bookingStatus(b.getBookingStatus())
                .build()).toList();
    }

    @Transactional(rollbackFor =  Exception.class)
    @Override
    public String createBooking(BookingRequest request, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        //Valid subfield
        SubField subField = subFieldRepository.findById(request.getSubFieldId()).orElseThrow(() -> new ResourceNotFoundException("SubField not found"));

        if (!subField.getStatus().equals(ESubFieldStatus.AVAILABLE)) {
            throw new BookingException("Subfield not available");
        }

        //Valid date
        if (request.getDate().isBefore(LocalDate.now())) {
            throw new BookingException("Date must be after now");
        }

        //Valid time
        LocalDateTime startDateTime = LocalDateTime.of(request.getDate(), request.getStartTime());
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new BookingException("Invalid start time");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BookingException("Start time must be before end time");
        }

        if (request.getStartTime().isBefore(subField.getField().getOpenTime())) {
            throw new BookingException("Start time must be after open time");
        }

        if (request.getEndTime().isAfter(subField.getField().getCloseTime())) {
            throw new BookingException("End time must be before close time");
        }

        //Valid duration must be in field type
        if (request.getDuration() != java.time.Duration.between(request.getStartTime(), request.getEndTime()).toMinutes()) {
            throw new BookingException("Duration not valid");
        }

        Set<FieldTypeBookDuration> durations = subField.getField().getFieldType().getFieldTypeBookDuration();
        Set<Integer> validMinutes = durations.stream()
                .map(fieldTypeBookDuration -> fieldTypeBookDuration.getDuration().getMinutes())
                .collect(Collectors.toSet());

        if (!validMinutes.contains(request.getDuration())) {
            throw new ResourceNotFoundException("Duration not found in this field");
        }

        //Check overlap
        List<Booking> bookings = bookingRepository.findOverlappingBookingsForUpdate(request.getSubFieldId(), request.getDate(), request.getStartTime(), request.getEndTime());
        if (!bookings.isEmpty()) {
            throw new InvalidDataException("Giờ này trong sân đã bị đặt rồi, bạn chọn giờ khác nhé");
        }

        //Valid occupied
        List<FieldHasTimeSlot> slots = fieldHasTimeSlotRepository.findSlotsForBooking(subField.getField().getId(), request.getStartTime(), request.getEndTime());

        Booking booking = Booking.builder()
                .bookingDate(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .duration(request.getDuration())
                .subfield(subField)
                .user(user)
                .bookingStatus(EBookingStatus.PENDING)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();

        //BookingServiceItem
        if (request.getBookingServiceItemRequest() != null && !request.getBookingServiceItemRequest().isEmpty()) {
            for (BookingServiceItemRequest requestItem : request.getBookingServiceItemRequest()) {
                //field service item id check
                FieldServiceItem fsi = fieldServiceItemRepository.findById(requestItem.getFieldServiceItemId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy item của sân này"));

                //quantity check
                if (requestItem.getQuantity() < 0 || requestItem.getQuantity() > fsi.getQuantity()) {
                    throw new BookingException("Quantity not valid");
                }

                if (!fsi.getStatus().equals(EFieldServiceItem.ACTIVE)) {
                    throw new BookingException("Service Item is Inactive");
                }

                BookingServiceItem bookingServiceItem = BookingServiceItem.builder()
                        .fieldServiceItem(fsi)
                        .quantity(requestItem.getQuantity())
                        .booking(booking)
                        .subTotal(fsi.getPrice().multiply(BigDecimal.valueOf(requestItem.getQuantity())))
                        .build();
                //Set each booking service item for booking
                booking.getBookingServiceItems().add(bookingServiceItem);
            }
        }


        //
        BigDecimal totalSlotPrice = fieldHasTimeSlotService.calculateDynamicPrice(slots, request.getStartTime(), request.getEndTime());
        BigDecimal totalServiceItem = booking.getBookingServiceItems().stream().map(BookingServiceItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = totalSlotPrice.add(totalServiceItem);
        booking.setTotalPrice(total);

        if (request.getVoucherId() != null) {
            Voucher voucher = voucherRepository.findById(request.getVoucherId()).orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
            booking.setTotalPrice(voucherService.applyDiscount(total, voucher));

        }

        bookingRepository.save(booking);
        log.info("Created booking Id {}", booking.getId());
        return booking.getPaymentToken();
    }

    @Override
    public List<BookingListResponse> getAllBookings(String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        List<Booking> bookings = user.getBookings().stream()
                .sorted(Comparator.comparing(Booking::getId).reversed())
                .toList();
        return bookings.stream().map(b -> BookingListResponse.builder()
                .id(b.getId())
                .start(b.getStartTime())
                .end(b.getEndTime())
                .duration(b.getDuration())
                .total(b.getTotalPrice())
                .status(b.getBookingStatus())
                .fieldName(b.getSubfield().getField().getName())
                .build())
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelBooking(Long id, String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("Không thấy user"));

        Booking booking = bookingRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking"));

        try {
            if (isExpiredBooking(booking)) {
                throw new InvalidDataException("Booking is expired");
            }
        } catch (InvalidDataException e) {
            markAsExpired(id);
            throw e;
        }

        if (LocalDateTime.now().isAfter(LocalDateTime.of(booking.getBookingDate(), booking.getStartTime())) ||
                EBookingStatus.COMPLETED.equals(booking.getBookingStatus())) {
            throw new BookingException("Cannot cancel a booking that has already started or passed.");
        }

        if (EBookingStatus.PENDING.equals(booking.getBookingStatus()) || EBookingStatus.PAID.equals(booking.getBookingStatus())) {
            booking.setBookingStatus(EBookingStatus.CANCELLED);
            bookingRepository.save(booking);
            log.info("Canceled booking id: {}", booking.getId());
        } else {
            throw new BookingException("Cannot cancel booking with status: " + booking.getBookingStatus());
        }

        if (booking.getChallengeMatch() != null) {
            challengeMatchService.cancelMatch(booking.getChallengeMatch().getId(), name);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsExpired(Long id) {
        Booking booking = findBooking(id);
        booking.setBookingStatus(EBookingStatus.FAILED);
        bookingRepository.save(booking);
    }

    @Override
    public List<BookingForUserResponse> getBookingsForUser(String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Booking> bookings = bookingRepository.findByUserId(user.getId())
                .stream()
                .sorted(Comparator.comparing(Booking::getId).reversed())
                .toList();

        return bookings.stream().map(b -> BookingForUserResponse.builder()
                .id(b.getId())
                .date(b.getBookingDate())
                .fieldName(b.getSubfield().getField().getName())
                .startTime(b.getStartTime())
                .endTime(b.getEndTime())
                .paymentMethod(b.getPaymentMethod().name())
                .status(String.valueOf(b.getBookingStatus()))
                .cancel(b.getBookingStatus() == EBookingStatus.PENDING || b.getBookingStatus() == EBookingStatus.PAID)
                .canReview(b.getBookingStatus() == EBookingStatus.COMPLETED && (b.getReview() == null || b.getReview().getRating() == null))
                .canCreateMatch((b.getBookingStatus().equals(EBookingStatus.PAID) && b.getChallengeMatch() == null))
                .challengeMatchTypeResponse(challengeMatchTypeService.getAllChallengeMatchTypesByFieldType(b.getSubfield().getField().getFieldType().getId()))
                .totalPrice(b.getTotalPrice())
                .rating(Optional.ofNullable(b.getReview())
                        .map(Review::getRating)
                        .orElse(null))
                .build()).toList();
    }

    @Override
    public BookingForUserResponse getBookingForUser(Long id, String name) {

        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        return BookingForUserResponse.builder()
                .id(booking.getId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .fieldBanner(booking.getSubfield().getField().getBanner())
                .fieldName(booking.getSubfield().getField().getName())
                .subFieldName(booking.getSubfield().getName())
                .status(String.valueOf(booking.getBookingStatus()))
                .price(booking.getBookingServiceItems().stream().map(BookingServiceItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add))
                .canCreateMatch((booking.getBookingStatus().equals(EBookingStatus.PAID) && booking.getChallengeMatch() == null))
                .serviceItemName(
                        booking.getBookingServiceItems().stream()
                                .map(bsi -> Optional.ofNullable(bsi.getFieldServiceItem())
                                        .map(FieldServiceItem::getServiceItem)
                                        .map(ServiceItem::getName)
                                        .orElse("Không có dịch vụ kèm theo"))
                                .collect(Collectors.joining(", "))
                )
                .build();
    }

    @Override
    public boolean isExpiredBooking(Booking booking) {
        if (booking.getBookingStatus().equals(EBookingStatus.PENDING) && booking.getExpiredAt().isBefore(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    @Override
    public List<Integer> getAvailableDurationsBooking(Long id, LocalDate date, LocalTime startTime) {
        SubField subField = subFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subfield not found"));

        List<Booking> bookings = bookingRepository.findBySubfieldAndBookingDate(subField, date)
                .stream()
                .filter(booking -> !booking.getBookingStatus().equals(EBookingStatus.CANCELLED))
                .toList();

        List<Duration> durations = fieldTypeBookDurationRepository.findDurationByFieldTypeId(subField.getField().getFieldType().getId());

        List<Integer> availableDurations = durations.stream()
                .map(Duration::getMinutes)
                .filter(d -> isDurationAvailable(bookings, startTime, d, subField))
                .toList();

        return availableDurations;
    }

    @Override
    public List<SlotAvailableResponse> getAvailableSlots(Long id, LocalDate date) {

        SubField subField = subFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subfield not found"));

        List<Booking> bookings = bookingRepository.findBySubfieldAndBookingDate(subField, date);

        LocalTime openTime = subField.getField().getOpenTime();
        LocalTime closeTime = subField.getField().getCloseTime();

        List<SlotAvailableResponse> slots = new ArrayList<>();

        for (LocalTime time = openTime; time.isBefore(closeTime.minusMinutes(SLOT_INTERVAL)); time = time.plusMinutes(SLOT_INTERVAL)) {
            LocalTime currentTime = time;
            boolean isBooked = bookings.stream().anyMatch(b ->
                    !b.getBookingStatus().equals(EBookingStatus.CANCELLED)
                            && (currentTime.isBefore(b.getEndTime())
                            && currentTime.plusMinutes(SLOT_INTERVAL).isAfter(b.getStartTime())));
            slots.add(SlotAvailableResponse.builder()
                    .startTime(time)
                    .isAvailable(!isBooked)
                    .build());
        }
        return slots;
    }

    private boolean isDurationAvailable(List<Booking> bookings, LocalTime startTime, Integer d, SubField subField) {
        LocalTime endTime = startTime.plusMinutes(d);

        for (Booking booking : bookings) {
            if (startTime.isBefore(booking.getEndTime()) && endTime.isAfter(booking.getStartTime())) {
                return false;
            }
        }

        if (subField.getField().getCloseTime().isBefore(endTime)) {
            return false;
        }

        return true;
    }

    @Override
    public Booking findBooking(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

}
