package com.fullstack.dto;

import com.fullstack.util.PhoneNumberUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Vui long nhap so dien thoai")
    @Pattern(regexp = PhoneNumberUtils.PHONE_REGEX, message = PhoneNumberUtils.PHONE_MESSAGE)
    private String phoneNumber;

    @NotBlank(message = "Vui long nhap mat khau")
    private String password;
}
