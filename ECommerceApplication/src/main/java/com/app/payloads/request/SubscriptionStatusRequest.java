package com.app.payloads.request;

import com.app.entites.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionStatusRequest {

    @JsonIgnore
    private Long vendorId;
    @JsonIgnore
    private Long subscriptionId;
    @NotNull(message = "Customer ID is required") private Long customerId;
    @NotNull(message = "Customer ID is required") private SubscriptionStatus status;
    // Getters and Setters
}
