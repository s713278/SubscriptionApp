package com.app.payloads.request;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class VendorProfileRequest implements Serializable {

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
    private String communicationEmail;

    @JsonProperty("business_address")
    private  Map<String, String> businessAddress;

    @JsonProperty("service_areas")
    private Map<String, Object> serviceAreas;

    @JsonProperty("banner_image")
    private String bannerImage;

    @JsonIgnore
    private Long userId;

}
