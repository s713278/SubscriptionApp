package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OTPRequest{
@JsonIgnore String email;

    @NotNull @Schema(description = "Country Code", example = "+91")
    @JsonProperty("country_code")
    private String  countryCode;

    @NotNull @JsonProperty("mobile_number") Long mobile ;

    @NotNull @Schema(description = "source", example = "android")
    @NotNull @JsonProperty("reg_source") String regSource ;

    @JsonIgnore
    private String fullPhoneNumber;

    public String getFullPhoneNumber(){
        return countryCode+mobile;
    }
}
