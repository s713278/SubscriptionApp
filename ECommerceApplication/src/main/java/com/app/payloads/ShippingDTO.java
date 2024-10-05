package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ShippingDTO {

    @Schema(description = "Shipping Type", example = "INSTORE_PICKUP")
    @JsonProperty("shipping_type")
    private String shippingMethod;

    private AddressDTO address;
}
