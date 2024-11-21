package com.app.payloads;

import java.time.LocalDate;
import java.util.List;

import com.app.entites.SubscriptionStatus;
import com.app.entites.type.SubFrequency;

public record SubscriptionDTO(Long id, String skuName, double price, String skuSize, SubscriptionStatus status,
                              int quantity, SubFrequency frequency, List<Integer> customDays, LocalDate fromStartDate,
                              LocalDate nextDeliveryDate) {

}
