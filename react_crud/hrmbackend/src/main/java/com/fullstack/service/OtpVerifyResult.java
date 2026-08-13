package com.fullstack.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtpVerifyResult {

    public enum Outcome {
        SUCCESS,
        NOT_FOUND,
        EXPIRED,
        WRONG,      // sai nhung con luot thu
        EXHAUSTED   // lan sai cuoi cung, het luot thu
    }

    private final Outcome outcome;
    private final int attemptCount;
    private final int maxAttempts;

    public int getRemainingAttempts() {
        return Math.max(0, maxAttempts - attemptCount);
    }
}
