package com.Fisport.api;

import com.Fisport.dto.response.FieldResponse;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.model.FieldType;
import com.Fisport.service.FieldService;
import com.Fisport.service.FieldTypeService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/field-types")
@Validated
@RequiredArgsConstructor
public class FieldTypeController {

    private final FieldTypeService fieldTypeService;
    private final FieldService fieldService;

    @GetMapping
    public ResponseData<?> getAllFieldTypes() {
        return new ResponseData<>(HttpStatus.OK.value(), "Get All Field Types", fieldTypeService.findAll());
    }

    @GetMapping("/{id}/fields")
    public ResponseData<?> getFieldsByType(@Min(1) @PathVariable long id) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get Field Types", fieldService.getFieldByFieldTypeId(id));
    }
}
