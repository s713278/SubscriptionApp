package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OTPVerificationRequest {
    @JsonProperty("mobile_number")
    private Long mobile;

    @NotBlank(message="OTP is required!")
    @JsonProperty("otp")
    private String otp;

    @JsonIgnore
    @JsonProperty("email")
    private String email;

    // Getters and Setters
}
