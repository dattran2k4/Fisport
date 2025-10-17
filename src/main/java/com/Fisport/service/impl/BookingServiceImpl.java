package com.Fisport.service.impl;

import com.Fisport.common.EBookingStatus;
import com.Fisport.common.ESubFieldStatus;
import com.Fisport.dto.request.BookingRequest;
import com.Fisport.dto.request.BookingServiceItemRequest;
import com.Fisport.dto.response.*;
import com.Fisport.exception.InvalidDataException;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.*;
import com.Fisport.repository.*;
import com.Fisport.service.BookingService;
import com.Fisport.service.FieldHasTimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SubFieldRepository subFieldRepository;
    private final FieldServiceItemRepository fieldServiceItemRepository;
    private final FieldHasTimeSlotRepository fieldHasTimeSlotRepository;
    private final UserRepository userRepository;
    private final FieldHasTimeSlotService fieldHasTimeSlotService;

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

    @Transactional
    @Override
    public String createBooking(BookingRequest request, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //Valid subfield
        SubField subField = subFieldRepository.findById(request.getSubFieldId()).orElseThrow(() -> new ResourceNotFoundException("SubField not found"));

        if (!subField.getStatus().equals(ESubFieldStatus.AVAILABLE)) {
            throw new InvalidDataException("Subfield not available");
        }

        //Valid date
        if (request.getDate().isBefore(LocalDate.now())) {
            throw new InvalidParameterException("Date must be after now");
        }

        //Valid time
        LocalDateTime startDateTime = LocalDateTime.of(request.getDate(), request.getStartTime());
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidParameterException("Invalid start time");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidParameterException("Start time must be before end time");
        }

        if (request.getStartTime().isBefore(subField.getField().getOpenTime())) {
            throw new InvalidParameterException("Start time must be after open time");
        }

        if (request.getEndTime().isAfter(subField.getField().getCloseTime())) {
            throw new InvalidParameterException("End time must be before close time");
        }

        //Valid duration must be in field type
        if (request.getDuration() != Duration.between(request.getStartTime(), request.getEndTime()).toMinutes()) {
            throw new InvalidParameterException("Duration not valid");
        }

        Set<FieldTypeBookDuration> durations = subField.getField().getFieldType().getFieldTypeBookDuration();
        Set<Integer> validMinutes = durations.stream()
                .map(fieldTypeBookDuration -> fieldTypeBookDuration.getDuration().getMinutes())
                .collect(Collectors.toSet());

        if (!validMinutes.contains(request.getDuration())) {
            throw new InvalidParameterException("Duration not valid");
        }

        //Valid occupied
        List<FieldHasTimeSlot> slots = fieldHasTimeSlotRepository.findSlotsForBooking(subField.getField().getId(), request.getStartTime(), request.getEndTime());
        if (slots.isEmpty()) {
            throw new ResourceNotFoundException("Slot not found");
        }

        Booking booking = Booking.builder()
                .bookingDate(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .duration(request.getDuration())
                .subfield(subField)
                .user(user)
                .bookingStatus(EBookingStatus.PENDING)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        //BookingServiceItem
        if (request.getBookingServiceItemRequest() != null && !request.getBookingServiceItemRequest().isEmpty()) {
            for (BookingServiceItemRequest requestItem : request.getBookingServiceItemRequest()) {
                //field service item id check
                FieldServiceItem fsi = fieldServiceItemRepository.findById(requestItem.getFieldServiceItemId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy item của sân này"));

                //quantity check
                if (requestItem.getQuantity() < 0 || requestItem.getQuantity() > fsi.getQuantity()) {
                    throw new InvalidParameterException("Quantity not valid");
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

        bookingRepository.save(booking);

        return booking.getPaymentToken();
    }

    @Override
    public List<BookingListResponse> getAllBookings(String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Booking> bookings = user.getBookings().stream().toList();
        return bookings.stream().map(b -> BookingListResponse.builder()
                .id(b.getId())
                .start(b.getStartTime())
                .end(b.getEndTime())
                .duration(b.getDuration())
                .total(b.getTotalPrice())
                .status(b.getBookingStatus())
                .fieldName(b.getSubfield().getField().getName())
                .build()).toList();
    }

    @Override
    public BookingDetailResponse getBooking(Long id, String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        PaymentResponse response = null;
        if (booking.getPayments() != null && !booking.getPayments().isEmpty()) {
            Payment last = booking.getPayments().stream()
                    .max(Comparator.comparing(Payment::getCreateAt))
                    .orElse(null);
            response.setStatus(last.getStatus());
            response.setMethod(last.getMethod());
            response.setPaymentAt(last.getPaymentTime());
        }

        return BookingDetailResponse.builder()
                .id(booking.getId())
                .date(booking.getBookingDate())
                .start(booking.getStartTime())
                .end(booking.getEndTime())
                .duration(booking.getDuration())
                .total(booking.getTotalPrice())
                .payment(response)
                .build();
    }

    @Override
    public void cancelBooking(Long id, String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (LocalDateTime.now().isAfter(LocalDateTime.of(booking.getBookingDate(), booking.getStartTime()))) {
            return;
        }

        booking.setBookingStatus(EBookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public List<BookingForUserResponse> getBookingsForUser(String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Booking> bookings = user.getBookings().stream().toList();

        return bookings.stream().map(b -> BookingForUserResponse.builder()
                .id(b.getId())
                .date(b.getBookingDate())
                .fieldName(b.getSubfield().getField().getName())
                .paymentMethod(b.getPayments().stream().map(Payment::getMethod).map(Object::toString).collect(Collectors.joining(",")))
                .status(String.valueOf(b.getBookingStatus()))
                .cancel(b.getBookingStatus() == EBookingStatus.PENDING || b.getBookingStatus() == EBookingStatus.PAID)
                .canReview(b.getBookingStatus() == EBookingStatus.COMPLETED)
                .totalPrice(b.getTotalPrice())
                .build()).toList();
    }

    @Override
    public BookingForUserResponse getBookingForUser(Long id, String name) {

        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = Optional.ofNullable(bookingRepository.findByIdAndUser(id, user))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        return BookingForUserResponse.builder()
                .id(booking.getId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .subFieldName(booking.getSubfield().getName())
                .price(booking.getBookingServiceItems().stream().map(BookingServiceItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add))
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

}
