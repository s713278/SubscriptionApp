package com.app.payloads;

import com.app.entites.type.SubFrequency;
import lombok.Data;

import java.util.List;

@Data
public class SubscriptionItemDto {
    private Long skuId;
    private int quantity;
    private SubFrequency frequency;
    private List<Integer> customDays; // For custom date range
    
    // Getters and Setters
}