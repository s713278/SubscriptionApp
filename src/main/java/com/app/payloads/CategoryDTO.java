package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private String type;

  @JsonProperty("description")
  private String description;

  public CategoryDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
