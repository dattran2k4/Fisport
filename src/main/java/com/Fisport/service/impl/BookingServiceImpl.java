package com.Fisport.service.impl;

import com.Fisport.common.EBookingStatus;
import com.Fisport.dto.request.BookingRequest;
import com.Fisport.dto.request.BookingServiceItemRequest;
import com.Fisport.dto.response.BookingTimeResponse;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.*;
import com.Fisport.repository.BookingRepository;
import com.Fisport.repository.FieldHasTimeSlotRepository;
import com.Fisport.repository.FieldServiceItemRepository;
import com.Fisport.repository.SubFieldRepository;
import com.Fisport.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SubFieldRepository subFieldRepository;
    private final FieldServiceItemRepository fieldServiceItemRepository;
    private final FieldHasTimeSlotRepository  fieldHasTimeSlotRepository;

    @Override
    public List<BookingTimeResponse> getOccupiedSlots(Long subFieldId, LocalDate date) {
        List<Booking> bookings = bookingRepository.findOccupiedSlots(subFieldId, date, List.of(EBookingStatus.PENDING, EBookingStatus.PAID, EBookingStatus.COMPLETED)
        );
        return bookings.stream().map(b -> BookingTimeResponse.builder()
                .startTime(b.getStartTime())
                .endTime(b.getEndTime())
                .bookingStatus(b.getBookingStatus())
                .build()).toList();
    }

    @Transactional
    @Override
    public void createBooking(BookingRequest request, Long userId) {
        //Valid subfield
        SubField subField = subFieldRepository.findById(request.getSubFieldId()).orElseThrow(() -> new ResourceNotFoundException("SubField not found"));

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
            throw new InvalidParameterException("Start time must be after end time");
        }

        if (request.getStartTime().isBefore(subField.getField().getOpenTime())) {
            throw new InvalidParameterException("Start time must after open time");
        }

        if (request.getEndTime().isAfter(subField.getField().getCloseTime())) {
            throw new InvalidParameterException("End time must before close time");
        }

        //Valid duration must be in field type
        if (request.getDuration() != Duration.between(request.getStartTime(), request.getEndTime()).toMinutes()) {
            throw new InvalidParameterException("Duration not valid");
        }

        if (!subField.getField().getFieldType().getFieldTypeBookDuration().contains(request.getDuration())) {
            throw new InvalidParameterException("Duration not valid");
        }

        //Valid occupied
        List<FieldHasTimeSlot> slots = fieldHasTimeSlotRepository.findSlotsForBooking(request.getSubFieldId(), request.getStartTime(), request.getEndTime());
        if (slots.isEmpty()) {
            throw new  ResourceNotFoundException("Slot not found");
        }

        Booking booking = Booking.builder()
                .bookingDate(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .duration(request.getDuration())
                .subfield(subField)
                .bookingStatus(EBookingStatus.PENDING)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        Set<BookingServiceItem> bookingServiceItems = new HashSet<>();

        //BookingServiceItem
        if (request.getBookingServiceItemRequest() != null && !request.getBookingServiceItemRequest().isEmpty()) {
            for (BookingServiceItemRequest requestItem : request.getBookingServiceItemRequest()) {
                //field service item id check
                FieldServiceItem fsi = fieldServiceItemRepository.findById(requestItem.getFieldServiceItemId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy item của sân này")))

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
                bookingServiceItems.add(bookingServiceItem);
            }
        }

        //
        BigDecimal totalSlotPrice = slots.stream().map(FieldHasTimeSlot::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalServiceItem = bookingServiceItems.stream().map(BookingServiceItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = totalSlotPrice.add(totalServiceItem);
        booking.setTotalPrice(total);

        bookingRepository.save(booking);
    }
}
