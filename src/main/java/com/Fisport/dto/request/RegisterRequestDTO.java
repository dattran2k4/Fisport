package com.Fisport.dto.request;

import com.Fisport.dto.validator.EnumValue;
import com.Fisport.common.EGender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequestDTO implements Serializable {

    @NotBlank(message = "Tài khoản không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "Mật khẩu xác nhận không được để trống")
    private String confirmPassword;

    @NotBlank(message = "Địa chỉ email không được để trống")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Định dạng email không hợp lệ"
    )
    @Email
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^\\+?[0-9\\s\\-()]{7,20}$",
            message = "Định dạng số điện thoại không hợp lệ"
    )
    private String phone;

    @NotNull(message = "Ngày bạn đẻ đừng để trống mà!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Ngày sinh phải trước hôm nay")
//    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;

    @NotNull(message = "Vui lòng chọn giới tính (chỉ có 2)")
    @EnumValue(name = "gender", enumClass = EGender.class)
    private EGender gender;
}
