package com.app.payloads;

import java.io.Serializable;
import java.util.Map;

import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.VendorStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import lombok.Data;


@Data
public class VendorDetailsDTO  implements Serializable {

    @JsonIgnore
    @JsonProperty("business_name")
    private String businessName;

    @JsonProperty("business_type")
    private String businessType;



    @JsonProperty("owner_name")
    private String ownerName;

    @JsonProperty("contact_person")
    private String contactPerson;

    @JsonProperty("description")
    private String description;

    @JsonProperty("contact_number")
    private String contactNumber;

    @JsonProperty("customer_care_number")
    private String customerCareNumber;

    @JsonProperty("communication_email")
    private String email;

    @JsonProperty("business_address")
    private  Map<String, String> businessAddress;

    @JsonIgnore
    private VendorStatus status;

    @JsonIgnore
    @JsonProperty("approve_status")
    private ApprovalStatus verificationStatus;

    @JsonProperty("service_areas")
    private Map<String, Object> serviceAreas;

    @Column(name="banner_image")
    private String bannerImage;
}
