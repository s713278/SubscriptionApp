package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OTPRequest(@JsonIgnore String email, @JsonProperty("mobile_number") Long mobile) {

}
