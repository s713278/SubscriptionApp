package com.app.payloads.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record NameAndAddressRequest(@NotBlank(message = "Name is required.") String name, Map<String,String> address) {
}
