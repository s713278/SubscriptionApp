package com.app.payloads.request;

import com.app.entites.type.UserRoleEnum;
import com.app.services.constants.UserRegPlatform;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request for user registration")
public class SignUpRequest {
  /*
  //    @NotBlank(message = "First name is required.")
      @JsonProperty("first_name")
      @Schema(description = "First name", example = "Rama")
      private String firstName;

      @JsonIgnore
      @Schema(description = "Password", example = "P@ssword123")
      @Size(min = 4,max = 20,message = "Password should be in the range of 4 to 20 characters.")
      @NotBlank(message = "Password is required.")
      @JsonProperty("password")
      private String password="909090";
  */
  @NotNull
  @Schema(description = "Registration Source", example = "Android")
  @JsonProperty("reg_platform")
  private UserRegPlatform regPlatform;

  // @NotNull
  @JsonIgnore
  @Schema(description = "type", example = "USER or VENDOR")
  @JsonProperty("user_role")
  private UserRoleEnum userRoleEnum;
}
