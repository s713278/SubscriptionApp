package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SignUpDTO(
        @JsonProperty("customer_id") Long userId,
        @JsonProperty("mobile_verified")
        boolean mobileVerified,
        @JsonProperty("email_verified")
        boolean emailVerified,
        String message) {


    public SignUpDTO(Long userId, String message) {
        this(userId, false, false, message);
    }
}
