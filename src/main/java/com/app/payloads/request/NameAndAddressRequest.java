package com.app.payloads.request;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;

public record NameAndAddressRequest(@NotBlank(message = "Name is required.") String name, Map<String,String> address) {
}
