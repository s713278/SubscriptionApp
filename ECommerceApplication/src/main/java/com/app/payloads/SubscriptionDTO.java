package com.app.payloads;

import java.time.LocalDate;
import java.util.List;

import com.app.entites.SubscriptionFrequency;
import com.app.entites.SubscriptionStatus;

public record SubscriptionDTO(Long id, String skuName, double price, String skuSize, SubscriptionStatus status,
        int quantity, SubscriptionFrequency frequency, List<Integer> customDays, LocalDate fromStartDate,
        LocalDate nextDeliveryDate) {

}
