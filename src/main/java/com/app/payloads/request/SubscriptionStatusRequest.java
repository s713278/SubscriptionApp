package com.app.payloads.request;

import com.app.entites.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionStatusRequest {

    @JsonIgnore
    private Long vendorId;
    @JsonIgnore
    private Long subscriptionId;

    @Schema(description = "Status of the subscription", example = "NEW")
    @NotNull(message = "Status is required") private SubscriptionStatus status;
    // Getters and Setters
}
