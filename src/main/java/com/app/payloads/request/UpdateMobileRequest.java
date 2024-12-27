package com.app.payloads.request;

import com.app.config.AppConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Update Mobile Request")
public class UpdateMobileRequest {

  @NotNull
  @Schema(
      description = "Mobile number",
      example = "9876543210",
      pattern = AppConstants.MOBILE_REGEX)
  @JsonProperty("old_mobile_number")
  private Long old_mobile;

  @JsonIgnore
  @JsonProperty("otp_code")
  private String otp;

  @NotNull
  @Schema(
      description = "Mobile number",
      example = "9090912345",
      pattern = AppConstants.MOBILE_REGEX)
  @JsonProperty("new_mobile_number")
  private Long new_mobile;
}
