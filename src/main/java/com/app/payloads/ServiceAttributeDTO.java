package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ServiceAttributeDTO implements Serializable {

  @Schema(example = "30")
  @JsonProperty(value = "valid_days", defaultValue = "30")
  private Integer validDays;

  @Schema(example = "1")
  @JsonProperty(value = "no_of_services", defaultValue = "1")
  private Integer noOfUses;
}
