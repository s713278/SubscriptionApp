package com.app.repositories.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface VendorProjection {

  @JsonProperty("id")
  Long getId();

  String getBusinessName();
}
