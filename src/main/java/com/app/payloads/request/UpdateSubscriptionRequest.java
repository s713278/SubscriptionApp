package com.app.payloads.request;

import java.time.LocalDate;

import com.app.entites.SubscriptionFrequency;
import com.app.entites.SubscriptionStatus;

import lombok.Data;


@Data
public class UpdateSubscriptionRequest {

    private Integer quantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionFrequency frequency;
    private SubscriptionStatus status;
    // Getters and Setters
}
