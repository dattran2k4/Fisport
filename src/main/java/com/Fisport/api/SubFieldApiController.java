package com.Fisport.api;

import com.Fisport.common.ESubFieldStatus;
import com.Fisport.dto.request.SubFieldRequest;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ResponseError;
import com.Fisport.dto.response.SubFieldResponse;
import com.Fisport.service.SubFieldService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sub-fields")
public class SubFieldApiController {

    private final SubFieldService subFieldService;

    @GetMapping
    public ResponseData<?> getAllSubFields(
            @RequestParam(required = false) Long fieldId,
            @RequestParam(required = false) ESubFieldStatus status) {
        try {
            List<SubFieldResponse> responses = subFieldService.getAllSubFields(fieldId, status);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Fields Success", responses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Get Fields Error");
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/create")
    public ResponseData<?> createSubField(@Valid @RequestBody SubFieldRequest subFieldRequest, Principal principal) {
        try {
            subFieldService.createSubField(subFieldRequest, principal.getName());
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Create SubField Success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create SubField Error");
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{id}/update")
    public ResponseData<?> updateSubField(@Min(1) @PathVariable Long id, @Valid @RequestBody SubFieldRequest subFieldRequest, Principal principal) {
        try {
            subFieldService.updateSubField(id, subFieldRequest, principal.getName());
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update SubField Success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update SubField Error");
        }
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("{id}/delete")
    public ResponseData<?> deleteSubField(@Min(1) @PathVariable Long id, Principal principal) {
        try {
            subFieldService.deleteSubField(id, principal.getName());
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Delete SubField Success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete SubField Error");
        }
    }
}
