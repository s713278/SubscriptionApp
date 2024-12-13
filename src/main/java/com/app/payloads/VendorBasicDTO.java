package com.app.payloads;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VendorBasicDTO(
        @JsonProperty("vendor_id") Long id,
     @JsonProperty("business_name")  String businessName,
     @JsonProperty("banner_image") String bannerImage,
     @JsonProperty("service_area") String serviceArea,
        @JsonProperty("category") List<String> categories
){
    public VendorBasicDTO() {
        this(0L, "Default Business", null, null, List.of());
    }
}
