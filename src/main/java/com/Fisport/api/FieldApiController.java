package com.Fisport.api;

import com.Fisport.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/field")
@RequiredArgsConstructor
public class FieldApiController {

    private final FieldService fieldService;
}
