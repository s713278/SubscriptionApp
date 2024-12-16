package com.app.payloads;

import java.io.Serializable;
import java.util.Map;

import com.app.entites.type.VendorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class VendorDetailsDTO  implements Serializable {

    @JsonProperty("business_name")
    private String businessName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("business_type")
    private String businessType;

    @JsonProperty("owner_name")
    private String ownerName;

    @JsonProperty("contact_person")
    private String contactPerson;

    @JsonProperty("contact_number")
    private String contactNumber;

    @JsonProperty("communication_email")
    private String email;

    @JsonProperty("business_address")
    private  Map<String, String> businessAddress;

    @JsonProperty("service_areas")
    private Map<String, Object> serviceAreas;

    @JsonProperty("banner_image")
    private String bannerImage;

    @JsonProperty("vendor_status")
    private VendorStatus status=VendorStatus.ACTIVE;

}
