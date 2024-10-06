package com.app.payloads.request;

import java.time.LocalDate;
import java.util.List;

import com.app.entites.SubscriptionFrequency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {
    
     @NotNull(message = "Customer ID is required") @JsonProperty("customer_id")
    private Long customerId;

    @JsonIgnore
    private Long vendorId;

    @NotNull(message = "SKU ID is required") @JsonProperty("sku_id")
    private Long skuId;

    @NotNull(message = "Quantity is required") private Integer quantity;

    @NotNull(message = "Frequency is required") private SubscriptionFrequency frequency;
    
    @NotNull(message = "Start Date is required") @JsonProperty("start_date")
    private LocalDate fromStartDate;
    
    @JsonProperty("custom_days")
    private List<Integer> customDays; // Optional: Applicable if frequency is CUSTOM
        
}
