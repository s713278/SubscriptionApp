package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Schema(description = "Request object for mobile registration")
public class MobileSignUpRequest extends SignUpRequest{

    @NotNull @Schema(description = "Customer's mobile number", example = "9876543210")
    @JsonProperty("mobile_number")
    private Long mobile;
    
    @JsonIgnore
    private String otp;

}
