package com.app.payloads;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.app.entites.type.VendorStatus;
import com.app.entites.type.VerificationStatus;

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
    private Map<String, String> serviceAreas;

    private VendorStatus status;
    private VerificationStatus verificationStatus;
}
