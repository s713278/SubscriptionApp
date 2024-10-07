package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SignUpResponse(
        @JsonProperty("customer_id") Long userId,String message) {
}
