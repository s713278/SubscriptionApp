package com.app.payloads.request;

import lombok.Data;

@Data
public class OTPVerificationRequest {
    private String emailOrMobile;
    private String otp;

    // Getters and Setters
}
