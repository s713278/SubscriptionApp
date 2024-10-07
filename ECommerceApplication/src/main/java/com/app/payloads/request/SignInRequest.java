package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @Schema(description = "Customer's Email", example = "ram.ayodhya@example.com")
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Schema(description = "Customer's password", example = "StrongP@ssword123")
    private String password;

    @JsonProperty("mobile_number")
    private String mobile;

}
