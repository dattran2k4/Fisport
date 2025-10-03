package com.Fisport.api;

import com.Fisport.dto.response.FeatureResponse;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ResponseError;
import com.Fisport.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/features")
public class FeatureApiController {

    private final FeatureService featureService;

    @GetMapping()
    public ResponseData<?> getAllFeatures() {
        try {
            List<FeatureResponse> responses = featureService.getListFeatures();
            return new ResponseData<>(HttpStatus.OK.value(), "get all features success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "get all features failed");
        }
    }
}
