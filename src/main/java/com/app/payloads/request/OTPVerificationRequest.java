package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OTPVerificationRequest {

    @NotNull @Schema(description = "Country Code", example = "+91")
    @JsonProperty("country_code")
    private String  countryCode;

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
