package com.app.payloads.request;

import com.app.config.AppConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Request object for mobile registration")
public class MobileSignUpRequest extends SignUpRequest {

  @NotNull
  @Schema(description = "Country Code", example = "+91")
  @JsonProperty("country_code")
  private String countryCode;

  @NotNull
  @Schema(
      description = "Mobile number",
      example = "9876543210",
      pattern = AppConstants.MOBILE_REGEX)
  @JsonProperty("mobile_number")
  private Long mobile;
}
