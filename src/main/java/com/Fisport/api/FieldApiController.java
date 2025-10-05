package com.Fisport.api;

import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.response.*;
import com.Fisport.service.FieldService;
import com.Fisport.common.EFieldStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/fields")
@RequiredArgsConstructor
@Validated
public class FieldApiController {

    private final FieldService fieldService;

    @GetMapping()
    public ResponseData<?> getAllFields(@RequestParam(required = false) Long wardId,
                                        @RequestParam(required = false) Long typeId,
                                        @RequestParam(required = false) EFieldStatus status,
                                        @RequestParam(required = false) String keyword,
                                        Principal principal,
                                        @RequestParam(required = false) Long... featureIds) {
        try {
            String username = (principal != null) ? principal.getName() : null;
            List<FieldResponse> fieldResponses = fieldService.getAllFields(wardId, typeId, status, keyword, username, featureIds);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Fields Success", fieldResponses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Get Fields Failed");
        }
    }

    @GetMapping("/{id}")
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

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/me")
    public ResponseData<?> getFeatureByOwner(Principal principal) {
        try {
            List<FieldResponse> fieldResponses = fieldService.getFieldByOwner(principal.getName());
            return new ResponseData<>(HttpStatus.OK.value(), "Get Field Success", fieldResponses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Feature By Owner Error");
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/add")
    public ResponseData<?> createFields(@Valid @RequestBody FieldRequest fieldRequest, Principal principal) {
        try {
            fieldService.createFieldByOwnerId(fieldRequest, principal.getName());
            return new ResponseData<>(HttpStatus.CREATED.value(), "Create Field Success");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Create Field Error", e.getMessage());
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{fieldId}")
    public ResponseData<?> updateField(@Valid @RequestBody FieldRequest fieldRequest, @PathVariable Long fieldId, @PathVariable Long ownerId) {
        try {
            fieldService.updateFieldByOwnerId(fieldRequest, fieldId, ownerId);
            return new ResponseData<>(HttpStatus.OK.value(), "Update Field Success");
        }
        catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Update Field Error", e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{fieldId}/approve")
    public ResponseData<?> approveFieldStatusByAdmin(@PathVariable Long fieldId, @RequestParam EFieldStatus fieldStatus) {
        try {
            fieldService.changeStatusFieldByAdmin(fieldId, fieldStatus);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update Field Status Success");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Update Field Status Error", e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public ResponseData<?> getPendingFields() {
        try {
            List<FieldResponse> responses = fieldService.getAllPendingFields();
            return new ResponseData<>(HttpStatus.OK.value(), "Get All Fields Success", responses);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Get All Fields Error", e.getMessage());
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/me/pending")
    public ResponseData<?> getPendingFieldsByOwner(Principal principal) {
        try {
            List<FieldResponse> responses = fieldService.getAllPendingFieldsByOwner(principal.getName());
            return new ResponseData<>(HttpStatus.OK.value(), "Get All Fields Success", responses);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Get All Fields Error", e.getMessage());
        }
    }
}
