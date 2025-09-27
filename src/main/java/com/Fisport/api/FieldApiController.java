package com.Fisport.api;

import com.Fisport.dto.response.FieldResponse;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ResponseError;
import com.Fisport.service.FieldService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/field")
@RequiredArgsConstructor
@Validated
public class FieldApiController {

    private final FieldService fieldService;

    @GetMapping()
    public ResponseData<?> getFieldByWardIdAndTypeId(@RequestParam long wardId, @RequestParam long typeId) {
        try {
            List<FieldResponse> fieldResponses = fieldService.getFieldByWardAndType(wardId, typeId);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Fields By Ward And Type Id Success", fieldResponses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Fields By Ward And Type Id Error");
        }
    }

}
