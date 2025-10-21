package com.Fisport.service.impl;

import com.Fisport.dto.response.FieldHasTimeSlotResponse;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.Field;
import com.Fisport.model.FieldHasTimeSlot;
import com.Fisport.repository.FieldHasTimeSlotRepository;
import com.Fisport.service.FieldHasTimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FieldHasTimeSlotServiceImpl implements FieldHasTimeSlotService {
    private final FieldHasTimeSlotRepository fieldHasTimeSlotRepository;

    @Override
    public List<FieldHasTimeSlotResponse> getTimeSlotAndPriceByFieldId(Long id) {
        List<FieldHasTimeSlot> fieldHasTimeSlots = fieldHasTimeSlotRepository.findByFieldId(id);
        return fieldHasTimeSlots.stream()
                .map(fieldHasTimeSlot -> FieldHasTimeSlotResponse.builder()
                        .id(fieldHasTimeSlot.getId())
                        .startTime(fieldHasTimeSlot.getTimeSlot().getStartTime())
                        .price(fieldHasTimeSlot.getPrice())
                        .build()).toList();
    }

    @Override
    public List<FieldHasTimeSlotResponse> getPriceTimeSlotBooking(Long fieldId, LocalTime start, LocalTime end) {
        List<FieldHasTimeSlot> fieldHasTimeSlots = fieldHasTimeSlotRepository.findSlotsForBooking(fieldId, start, end);
        return fieldHasTimeSlots.stream()
                .map(fieldHasTimeSlot -> FieldHasTimeSlotResponse.builder()
                        .id(fieldHasTimeSlot.getId())
                        .startTime(fieldHasTimeSlot.getTimeSlot().getStartTime())
                        .price(fieldHasTimeSlot.getPrice())
                        .build()).toList();
    }

    @Override
    public BigDecimal getTotalPriceSlotBooking(Long fieldId, LocalTime start, LocalTime end) {
        List<FieldHasTimeSlot> slots = fieldHasTimeSlotRepository.findSlotsForBooking(fieldId, start, end);
        if (slots.isEmpty()) {
            throw new ResourceNotFoundException("Slot not found");
        }
        return calculateDynamicPrice(slots, start, end);
    }

    /**
     * Tính tổng price dựa vào các slot đã query trong khoảng booking
     *
     * @param slots     danh sách slot của field trong khoảng booking
     * @param userStart startTime booking
     * @param userEnd   endTime booking
     * @return tổng price
     */
    public BigDecimal calculateDynamicPrice(List<FieldHasTimeSlot> slots, LocalTime userStart, LocalTime userEnd) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        LocalTime currentTime = userStart;

        for (FieldHasTimeSlot slot : slots) {
            LocalTime slotStart = slot.getTimeSlot().getStartTime();
            LocalTime slotEnd = slotStart.plusHours(1); // slot duration mặc định = 1h

            if (!slotEnd.isAfter(currentTime)) continue; // slot trước currentTime thì bỏ

            LocalTime overlapStart = currentTime.isAfter(slotStart) ? currentTime : slotStart;
            LocalTime overlapEnd = userEnd.isBefore(slotEnd) ? userEnd : slotEnd;

            long overlapMinutes = Duration.between(overlapStart, overlapEnd).toMinutes();
            if (overlapMinutes > 0) {
                BigDecimal priceForSlot = slot.getPrice()
                        .multiply(BigDecimal.valueOf(overlapMinutes))
                        .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
                totalPrice = totalPrice.add(priceForSlot);
            }

            currentTime = overlapEnd;
            if (!currentTime.isBefore(userEnd)) break;
        }

        return totalPrice;
    }
}
