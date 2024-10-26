package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OTPRequest(@JsonProperty("email") String email,@JsonProperty("mobile_number") Long mobile) {

}
