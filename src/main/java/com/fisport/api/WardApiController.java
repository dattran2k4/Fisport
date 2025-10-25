package com.fisport.api;

import com.fisport.dto.response.ResponseData;
import com.fisport.dto.response.ResponseError;
import com.fisport.dto.response.WardResponse;
import com.fisport.service.WardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wards")
@RequiredArgsConstructor
@Validated
public class WardApiController {

    private final WardService wardService;

//    @GetMapping("/")
//    public ResponseData<?> getAllWards() {
//        try {
//            List<WardResponse> responses = wardService.getAll();
//            return new ResponseData<>(HttpStatus.OK.value(),  "Wards found", responses);
//        } catch (Exception e) {
//            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Wards not found");
//        }
//    }

    @GetMapping
    public ResponseData<?> getWardByCityId(@Valid @Min(1) @RequestParam Long cityId) {
        try {
            List<WardResponse> wardResponseList = wardService.getAllWardsByCityId(cityId);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Ward By City Id Succesfully", wardResponseList);
        } catch(Exception ex) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        }
    }
}
