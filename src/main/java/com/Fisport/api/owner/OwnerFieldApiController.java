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

    public ResponseData<?> createFields(@Valid @RequestBody FieldRequest fieldRequest, @PathVariable Long ownerId) {

    }
}
