package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request for user registration")
public class SignUpRequest {

    @NotBlank(message = "First name is required.")
    @JsonProperty("first_name")
    @Schema(description = "First name", example = "Rama")
    private String firstName;

    @Schema(description = "Password", example = "P@ssword123")
    @Size(min = 4,max = 20,message = "Password should be in the range of 4 to 20 characters.")
    @NotBlank(message = "Password is required.")
    @JsonProperty("password")
    private String password;
}
