package com.app.payloads.request;

import java.time.LocalDate;

import com.app.entites.SubscriptionStatus;
import com.app.entites.type.SubFrequency;

import lombok.Data;


@Data
public class UpdateSubscriptionRequest {

    private Integer quantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubFrequency frequency;
    private SubscriptionStatus status;
    // Getters and Setters
}
