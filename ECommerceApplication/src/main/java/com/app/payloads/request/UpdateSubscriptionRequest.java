package com.app.payloads.request;

import java.time.LocalDate;

import com.app.entites.SubscriptionFrequency;
import com.app.entites.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UpdateSubscriptionRequest {
    
    @JsonIgnore
    private Long subscriptionId;

     @NotNull(message = "Customer ID is required") private Long customerId;

    @JsonIgnore
    private Long vendorId;

    @NotNull(message = "SKU ID is required") private Long skuId;

 
    private Integer quantity;

    private LocalDate startDate;

    private SubscriptionFrequency frequency;

    private SubscriptionStatus status;
    // Getters and Setters
}
