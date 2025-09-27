package com.Fisport.api.owner;

import com.Fisport.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner/fields")
@RequiredArgsConstructor
public class OwnerFieldApiController {

    private final FieldService fieldService;
}
