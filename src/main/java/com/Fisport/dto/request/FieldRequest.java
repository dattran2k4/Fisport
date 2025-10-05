package com.Fisport.dto.request;


import com.Fisport.dto.validator.EnumValue;
import com.Fisport.common.EFieldStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
public class FieldRequest implements Serializable {

    @NotBlank(message = "Tên sân không được để trống")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Vui lòng thêm banner")
    private String banner;

    @NotBlank(message = "Thêm mô tả chi tiết sân")
    private String description;

    @NotNull(message = "Chọn giờ mở cửa")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @NotNull(message = "Chọn giờ đóng cửa")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @EnumValue(name = "status", enumClass = EFieldStatus.class)
    private EFieldStatus status;

    private Long fieldTypeId;
    private Long wardId;


}
