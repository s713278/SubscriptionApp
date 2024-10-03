package com.app.payloads;

import com.app.entites.SubscriptionFrequency;
import java.util.List;
import lombok.Data;

@Data
public class SubscriptionItemDto {
    private Long productId;
    private int quantity;
    private SubscriptionFrequency frequency;
    private List<Integer> customDays; // For custom date range
    
    // Getters and Setters
}