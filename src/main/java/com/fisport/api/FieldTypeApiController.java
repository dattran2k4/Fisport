package com.fisport.api;

import com.fisport.dto.response.ResponseData;
import com.fisport.service.FieldTypeBookDurationService;
import com.fisport.service.FieldTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/field-types")
@Validated
@RequiredArgsConstructor
public class FieldTypeApiController {

    private final FieldTypeService fieldTypeService;
    private final FieldTypeBookDurationService fieldTypeBookDurationService;

    @GetMapping
    public ResponseData<?> getAllFieldTypes() {
        return new ResponseData<>(HttpStatus.OK.value(), "Get All Field Types", fieldTypeService.findAll());
    }

//    @GetMapping("/{id}/fields")
//    public ResponseData<?> getFieldsByType(@Min(1) @PathVariable long id) {
//        return new ResponseData<>(HttpStatus.OK.value(), "Get Field Types", fieldService.getFieldByFieldTypeId(id));
//    }
    @GetMapping("/{id}/durations")
    public ResponseData<?> getDurationById(@PathVariable Long id) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get durations by field type id succes", fieldTypeBookDurationService.getDurationByFieldTypeId(id));
    }

}
