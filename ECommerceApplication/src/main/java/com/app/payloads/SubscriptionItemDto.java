package com.app.payloads;

import java.util.List;

import com.app.entites.SubscriptionFrequency;

import lombok.Data;

@Data
public class SubscriptionItemDto {
    private Long skuId;
    private int quantity;
    private SubscriptionFrequency frequency;
    private List<Integer> customDays; // For custom date range
    
    // Getters and Setters
}