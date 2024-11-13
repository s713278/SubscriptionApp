package com.app.payloads;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.app.entites.type.VendorStatus;
import com.app.entites.type.VerificationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VendorDTO implements Serializable {
    private Long id;
    @JsonProperty("business_name")
    private String businessName;
    private String ownerName;
    private List<CategoryDTO> categories;
    private String contactNumber;
    private String email;
    @JsonProperty("business_address")
    private Map<String, String> businessAddress;
    @JsonProperty("service_areas")
    private Map<String, String> serviceAreas;

    private VendorStatus status;
    @JsonProperty("verification_status")
    private VerificationStatus verificationStatus;
}
