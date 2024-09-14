package com.app.payloads.request;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String email;
    private String otp;

    // Getters and Setters
}
