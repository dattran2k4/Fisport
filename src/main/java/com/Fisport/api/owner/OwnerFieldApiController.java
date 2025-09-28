package com.Fisport.api.owner;

import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.service.FieldService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner/{ownerId}/fields")
@RequiredArgsConstructor
public class OwnerFieldApiController {

    private final FieldService fieldService;

    @GetMapping()
    public ResponseData<?> getAllFieldsByOwner(@PathVariable Long ownerId) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get All Fields", fieldService.getFieldByOwnerId(ownerId));
    }

    @PostMapping()
    public ResponseData<?> createFields(@Valid @RequestBody FieldRequest fieldRequest, @PathVariable Long ownerId) {
        try {
            fieldService.createFieldByOwnerId(fieldRequest, ownerId);
            return new ResponseData<>(HttpStatus.OK.value(), "Create Field Success");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Create Field Error", e.getMessage());
        }
    }

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
}
