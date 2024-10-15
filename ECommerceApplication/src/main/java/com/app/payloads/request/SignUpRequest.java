package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request object for customer registration")
public class SignUpRequest {

    @NotBlank(message = "First name is required.")
    @JsonProperty("first_name")
    @Schema(description = "Customer's first name", example = "Rama")
    private String firstName;

    @Schema(description = "Customer's password", example = "StrongP@ssword123")
    @NotBlank(message = "First name is required.")
    @JsonProperty("password")
    private String password;
}
