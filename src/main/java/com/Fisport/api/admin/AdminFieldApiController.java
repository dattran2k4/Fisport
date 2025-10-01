package com.Fisport.api.admin;


import com.Fisport.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/fields")
public class AdminFieldApiController {

    private final FieldService fieldService;


}
