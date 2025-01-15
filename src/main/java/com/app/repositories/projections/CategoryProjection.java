package com.app.repositories.projections;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface CategoryProjection {

  @JsonProperty("id")
  Long getId();

  @JsonProperty("ref_id")
  Long getCategoryRefId();

  String getName();

  @JsonProperty("image_path")
  String getImagePath();
}
