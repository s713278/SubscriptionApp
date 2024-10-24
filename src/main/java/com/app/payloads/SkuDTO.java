package com.app.payloads;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuDTO implements Serializable {
    private Long id;
    private String name;
    private String imagePath;
    private String description;
    private String size;
    private String skuCode;
}
