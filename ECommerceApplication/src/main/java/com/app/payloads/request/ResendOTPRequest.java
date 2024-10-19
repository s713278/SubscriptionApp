package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResendOTPRequest(@JsonProperty("mobile_number") String emailOrMobile) {

}
