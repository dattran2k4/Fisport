package com.Fisport.api;

import com.Fisport.dto.response.FieldResponse;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ResponseError;
import com.Fisport.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/field")
@RequiredArgsConstructor
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
