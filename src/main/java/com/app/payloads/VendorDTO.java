package com.app.payloads;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class VendorDTO implements Serializable {
    private Long id;
    private String businessName;
    private String ownerName;
    private List<CategoryDTO> categories;
    private String contactNumber;
    private String email;
    private Map<String, String> businessAddress;
}
