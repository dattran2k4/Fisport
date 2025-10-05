package com.Fisport.api;

import com.Fisport.common.ESubFieldStatus;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ResponseError;
import com.Fisport.dto.response.SubFieldResponse;
import com.Fisport.service.SubFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
            return new ResponseData<>(HttpStatus.OK.value(), "Get Fields Success",  responses);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Get Fields Error");
        }
    }
}
