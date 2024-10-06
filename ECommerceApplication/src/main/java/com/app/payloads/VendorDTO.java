package com.app.payloads;

import java.util.List;

import lombok.Data;

@Data
public class VendorDTO {

    private Long id;

    private String name;

    private List<CategoryDTO> categories;

    private String phoneName;

    private String email;
}
