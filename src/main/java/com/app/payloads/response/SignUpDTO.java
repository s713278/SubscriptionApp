package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SignUpDTO(
        @JsonProperty("user_id") Long userId,
        @JsonProperty("mobile") Long mobile,
        @JsonProperty("mobile_verified")
        boolean mobileVerified,
        @JsonProperty("email_verified")
        boolean emailVerified,
        String message) {
    public SignUpDTO(Long userId, String message) {
        this(userId,null, false, false, message);
    }
}
