package com.app.payloads;

import java.util.List;

import com.app.entites.type.SubFrequency;

import lombok.Data;

@Data
public class SubscriptionItemDto {
    private Long skuId;
    private int quantity;
    private SubFrequency frequency;
    private List<Integer> customDays; // For custom date range
    
    // Getters and Setters
}