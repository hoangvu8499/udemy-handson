package com.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterResponse {

    private Long userId;
    private String phoneNumber;
    private String status;
    private long otpExpiresInSeconds;
}
