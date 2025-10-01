package com.Fisport.api;

import com.Fisport.dto.response.*;
import com.Fisport.service.FieldService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
@Validated
public class FieldApiController {

    private final FieldService fieldService;

    @GetMapping()
    public ResponseData<?> getFieldByWardIdAndTypeId(@RequestParam(required = false) long wardId, @RequestParam(required = false) long typeId) {
        try {
            List<FieldResponse> fieldResponses = fieldService.getFieldByWardAndType(wardId, typeId);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Fields By Ward And Type Id Success", fieldResponses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Fields By Ward And Type Id Error");
        }
    }

    @GetMapping("{id}")
    public ResponseData<?> getFieldById(@Min(1) @PathVariable Long id) {
        FieldResponse fieldResponse = fieldService.getField(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Get Field Success", fieldResponse);
    }

    @GetMapping("/{id}/timeslots")
    public ResponseData<?> getTimeSlotsByField(@Min(1) @PathVariable Long id) {
        try {
            List<FieldHasTimeSlotResponse> responses = fieldService.getTimeSlotAndPriceByFieldId(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Time Slots Success", responses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Time Slots By Field Id Error");
        }
    }

    @GetMapping("/{id}/features")
    public ResponseData<?> getFeaturesByField(@Min(1) @PathVariable Long id) {
        try {
            Set<FeatureResponse> responses = fieldService.getFeautresByField(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Features By Field Success", responses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Features By Field Id Error");
        }
    }
}
