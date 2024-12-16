package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @JsonProperty("category_name")
    private String name;

    @JsonProperty("description")
    private String description;


    // private List<ProductDTO> products = new ArrayList<>();
}
