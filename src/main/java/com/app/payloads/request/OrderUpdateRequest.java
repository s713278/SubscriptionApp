package com.app.payloads.request;

import com.app.entites.type.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateRequest {

    @Schema(description = "Old Status", example = "CREATED/PROCESSING/SHIPPED/DELIVERED")
    @JsonProperty("old_status")
    OrderStatus oldStatus;

    @Schema(description = "New Status", example = "PROCESSING")
    @JsonProperty("new_status")
    OrderStatus newSatus;

}
