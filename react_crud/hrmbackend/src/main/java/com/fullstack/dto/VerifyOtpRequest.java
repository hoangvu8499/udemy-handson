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
public class VerifyOtpRequest {

    @NotBlank(message = "Vui long nhap so dien thoai")
    @Pattern(regexp = PhoneNumberUtils.PHONE_REGEX, message = PhoneNumberUtils.PHONE_MESSAGE)
    private String phoneNumber;

    @NotBlank(message = "Vui long nhap ma OTP")
    @Size(max = 6, message = "Ma OTP toi da 6 ky tu")
    private String otpCode;
}
