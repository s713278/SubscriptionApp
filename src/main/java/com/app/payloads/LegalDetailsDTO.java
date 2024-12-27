package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LegalDetailsDTO {

  @JsonProperty("gst_number")
  private String GSTNumber;

  @JsonProperty("pan_number")
  private String PANNumber;

  @JsonProperty("reg_number")
  private String regNumber;
}
