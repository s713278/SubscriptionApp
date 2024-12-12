package com.app.payloads;

import java.io.Serializable;

import com.app.entites.type.VendorStatus;
import com.app.entites.type.VerificationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class VendorDetailsDTO  implements Serializable {

    @JsonProperty("vendor_id")
    private Long id;

    @JsonProperty("contact_number")
    private String contactNumber;

    @JsonProperty("description")
    private String description;

    @JsonIgnore
    @JsonProperty("business_name")
    private String businessName;

    @JsonProperty("support_email")
    private String email;

    private VendorStatus status;
    @JsonProperty("verification_status")
    private VerificationStatus verificationStatus;
}
