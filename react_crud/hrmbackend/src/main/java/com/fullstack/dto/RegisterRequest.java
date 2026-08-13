package com.fullstack.dto;

import com.fullstack.util.PhoneNumberUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Vui long nhap so dien thoai")
    @Pattern(regexp = PhoneNumberUtils.PHONE_REGEX, message = PhoneNumberUtils.PHONE_MESSAGE)
    private String phoneNumber;

    @NotBlank(message = "Vui long nhap mat khau")
    @Size(min = 8, max = 15, message = "Mat khau phai co tu 8 den 15 ky tu")
    @Pattern(regexp = ".*[A-Za-z].*", message = "Mat khau phai chua it nhat 1 chu cai")
    @Pattern(regexp = ".*[0-9].*", message = "Mat khau phai chua it nhat 1 chu so")
    @Pattern(regexp = ".*[^A-Za-z0-9].*",
            message = "Mat khau phai chua it nhat 1 ky tu dac biet (vi du: !@#$%^&*)")
    private String password;
}
