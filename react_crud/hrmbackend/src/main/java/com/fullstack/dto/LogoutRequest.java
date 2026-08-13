package com.fullstack.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class LogoutRequest {

    @NotBlank(message = "Vui long truyen refreshToken")
    private String refreshToken;
}
