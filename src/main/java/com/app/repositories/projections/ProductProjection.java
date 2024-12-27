package com.app.repositories.projections;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ProductProjection {

  @JsonProperty("id")
  Long getId();

  String getName();

  @JsonProperty("image_path")
  String getImagePath();
}
