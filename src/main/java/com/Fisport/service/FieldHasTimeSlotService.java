package com.Fisport.service;

import com.Fisport.dto.response.FieldHasTimeSlotResponse;
import com.Fisport.model.FieldHasTimeSlot;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public interface FieldHasTimeSlotService {
    List<FieldHasTimeSlotResponse> getTimeSlotAndPriceByFieldId(Long id);
    List<FieldHasTimeSlotResponse> getPriceTimeSlotBooking(Long id, LocalTime start, LocalTime end);
    BigDecimal getTotalPriceSlotBooking(Long id, LocalTime start, LocalTime end);
    BigDecimal calculateDynamicPrice(List<FieldHasTimeSlot> slots, LocalTime userStart, LocalTime userEnd);
}
