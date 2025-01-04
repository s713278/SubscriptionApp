package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SignUpDTO(
    @JsonProperty("user_id") Long userId,
    @JsonIgnore @JsonProperty("mobile_number") String mobile,
    @JsonProperty("mobile_verified") boolean mobileVerified) {
  public SignUpDTO(Long userId, String message) {
    this(userId, null, false);
  }
}
