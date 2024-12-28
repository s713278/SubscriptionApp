package com.app.payloads.request;

import com.app.config.AppConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

  @Pattern(
      regexp = "^[6-9]\\d{9}$",
      message = "Mobile number must be 10 digits and start with 6, 7, 8, or 9.")
  @NotNull
  @Schema(
      description = "Mobile number",
      example = "9876543210",
      pattern = AppConstants.MOBILE_REGEX)
  @JsonProperty("mobile_number")
  @Size(min = 10, max = 10, message = "Mobile number should be 10 digits.")
  private String mobile;
}
