package com.Fisport.api;

import com.Fisport.common.ESubFieldStatus;
import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.response.*;
import com.Fisport.repository.FieldHasTimeSlotRepository;
import com.Fisport.service.FieldHasTimeSlotService;
import com.Fisport.service.FieldService;
import com.Fisport.common.EFieldStatus;
import com.Fisport.service.FieldServiceItemService;
import com.Fisport.service.SubFieldService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/fields")
@RequiredArgsConstructor
@Validated
public class FieldApiController {

    private final FieldService fieldService;
    private final FieldServiceItemService fieldServiceItemService;
    private final SubFieldService subFieldService;
    private final FieldHasTimeSlotService fieldHasTimeSlotService;

    @GetMapping()
    public ApiResponse<?> getAllFields(@RequestParam(required = false) Long wardId,
                                       @RequestParam(required = false) Long typeId,
                                       @RequestParam(required = false) EFieldStatus status,
                                       @RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) Long... featureIds) {

        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("fields")
                .data(fieldService.getAllFields(wardId, typeId, status, keyword, featureIds))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getFieldById(@Min(1) @PathVariable Long id) {

        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("fields")
                .data(fieldService.getField(id))
                .build();
    }

    @GetMapping("/{id}/timeslots")
    public ApiResponse<?> getTimeSlotsByField(@Min(1) @PathVariable Long id) {

        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("Get Time Slots Success")
                .data(fieldHasTimeSlotService.getTimeSlotAndPriceByFieldId(id))
                .build();
    }

    @GetMapping("{id}/bookingtime-price")
    public ApiResponse<?> getPriceInRangeBooking(@Min(1) @PathVariable Long id, LocalTime startTime, LocalTime endTime) {

        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("Get Price In Range Success")
                .data(fieldHasTimeSlotService.getTotalPriceSlotBooking(id, startTime, endTime))
                .build();
    }

    @GetMapping("/{id}/features")
    public ResponseData<?> getFeaturesByField(@Min(1) @PathVariable Long id) {
        try {
            Set<FeatureResponse> responses = fieldService.getFeautresByField(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Features By Field Success", responses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

//    @PreAuthorize("hasRole('OWNER')")
//    @GetMapping("/me")
//    public ResponseData<?> getFeatureByOwner(Principal principal) {
//        try {
//            List<FieldResponse> fieldResponses = fieldService.getFieldByOwner(principal.getName());
//            return new ResponseData<>(HttpStatus.OK.value(), "Get Field Success", fieldResponses);
//        } catch (Exception e) {
//            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Feature By Owner Error");
//        }
//    }


    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/add")
    public ResponseData<?> createFields(@Valid @RequestBody FieldRequest fieldRequest, Principal principal) {
        try {
            fieldService.createFieldByOwnerId(fieldRequest, principal.getName());
            return new ResponseData<>(HttpStatus.CREATED.value(), "Create Field Success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create Field Error");
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{fieldId}")
    public ResponseData<?> updateField(@Valid @RequestBody FieldRequest fieldRequest, @PathVariable Long fieldId, @PathVariable Long ownerId) {
        try {
            fieldService.updateFieldByOwnerId(fieldRequest, fieldId, ownerId);
            return new ResponseData<>(HttpStatus.OK.value(), "Update Field Success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update Field Error");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{fieldId}/approve")
    public ResponseData<?> approveFieldStatusByAdmin(@PathVariable Long fieldId, @RequestParam EFieldStatus fieldStatus) {
        try {
            fieldService.changeStatusFieldByAdmin(fieldId, fieldStatus);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public ResponseData<?> getPendingFields() {
        try {
            List<FieldResponse> responses = fieldService.getAllPendingFields();
            return new ResponseData<>(HttpStatus.OK.value(), "Get All Fields Success", responses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/me/pending")
    public ResponseData<?> getPendingFieldsByOwner(Principal principal) {
        try {
            List<FieldResponse> responses = fieldService.getAllPendingFieldsByOwner(principal.getName());
            return new ResponseData<>(HttpStatus.OK.value(), "Get All Fields Success", responses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/{fieldId}/service-items")
    public ResponseData<?> getServiceItems(@Min(1) @PathVariable Long fieldId) {
        try {
            List<FieldServiceItemResponse> list = fieldServiceItemService.getAllByActive(fieldId);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Service Items Success", list);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/{id}/sub-fields")
    public ResponseData<?> getSubFields(@Min(1) @PathVariable Long id) {
        try {
            List<SubFieldResponse> subFieldResponses = subFieldService.getAllSubFields(id, ESubFieldStatus.AVAILABLE);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Sub Fields Success", subFieldResponses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/nearby")
    public ApiResponse<?> getFieldNearyBy(@RequestParam Double lat, @RequestParam Double lng, @RequestParam(defaultValue = "2") Double radius) {
        List<FieldDetailResponse> response = fieldService.getFieldsNearBy(lat, lng, radius);
        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("Get Fields Near By Success")
                .data(response)
                .build();
    }
}

