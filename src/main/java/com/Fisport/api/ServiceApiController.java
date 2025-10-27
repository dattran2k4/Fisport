package com.Fisport.api;

import com.Fisport.dto.request.ServiceRequest;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ServiceResponse;
import com.Fisport.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/services")
public class ServiceApiController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<ResponseData<List<ServiceResponse>>> getAllServices() {
        List<ServiceResponse> services = serviceService.getAllServices();
        return ResponseEntity.ok(
                ResponseData.<List<ServiceResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách dịch vụ thành công")
                        .data(services)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<ServiceResponse>> getServiceById(@PathVariable Long id) {
        ServiceResponse service = serviceService.getServiceById(id);
        return ResponseEntity.ok(
                ResponseData.<ServiceResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy thông tin dịch vụ thành công")
                        .data(service)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseData<ServiceResponse>> createService(@Valid @RequestBody ServiceRequest request) {
        ServiceResponse service = serviceService.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseData.<ServiceResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tạo dịch vụ thành công")
                        .data(service)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<ServiceResponse>> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequest request) {
        ServiceResponse service = serviceService.updateService(id, request);
        return ResponseEntity.ok(
                ResponseData.<ServiceResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cập nhật dịch vụ thành công")
                        .data(service)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok(
                ResponseData.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Xóa dịch vụ thành công")
                        .build()
        );
    }
}
