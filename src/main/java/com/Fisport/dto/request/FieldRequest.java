package com.Fisport.dto.request;


import com.Fisport.util.EFieldStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class FieldRequest implements Serializable {

    @NotBlank(message = "Tên sân không được để trống")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Vui lòng thêm banner")
    private String banner;

    private String slug;

    @NotBlank(message = "Thêm mô tả chi tiết sân")
    private String description;

    @NotBlank(message = "Chọn giờ mở cửa")
    private LocalDateTime openTime;

    @NotBlank(message = "Chọn giờ đóng cửa")
    private LocalDateTime closeTime;


    private EFieldStatus status;

    private Long fieldTypeId;
    private Long wardId;


}
