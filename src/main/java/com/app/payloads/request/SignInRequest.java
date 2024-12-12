package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @Schema(description = "Email", example = "ram.seetha@ayodhya.com")
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Schema(description = "Password", example = "P@ssword123")
    @NotBlank(message = "Password is required.")
    @JsonProperty("password")
    private String password;

    @NotNull @Schema(description = "Mobile number", example = "9876543210")
    @JsonProperty("mobile_number")
    private Long mobile;

    @NotNull @Schema(description = "Country Code", example = "+91")
    @JsonProperty("country_code")
    private String  countryCode;

}
