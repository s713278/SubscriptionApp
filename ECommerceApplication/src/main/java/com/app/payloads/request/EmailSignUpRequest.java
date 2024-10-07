package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Schema(description = "Request object for customer registration")
public class EmailSignUpRequest extends SignUpRequest {

    @Schema(description = "Customer's email address", example = "ram.ayodhya@example.com")
    private String email;
    
    @JsonIgnore
    private String otp;
    @JsonIgnore
    private String emailActivationtoken;

}
