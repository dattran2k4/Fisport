package com.fisport.api;

import com.fisport.dto.response.OwnerFieldServiceResponse;
import com.fisport.dto.response.ResponseData;
import com.fisport.security.CustomUserDetails;
import com.fisport.service.OwnerFieldServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/owner/field-services")
public class OwnerFieldServiceApiController {

    private final OwnerFieldServiceService ownerFieldServiceService;

    @GetMapping
    public ResponseEntity<ResponseData<List<OwnerFieldServiceResponse>>> getMyFieldServices(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Long ownerId = userDetails.getUser().getId();
        List<OwnerFieldServiceResponse> fieldServices = ownerFieldServiceService.getAllFieldServicesByOwner(ownerId);
        
        return ResponseEntity.ok(
                ResponseData.<List<OwnerFieldServiceResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách dịch vụ của Owner thành công")
                        .data(fieldServices)
                        .build()
        );
    }
}

