package com.app.payloads.response;

import com.app.entites.SubscriptionStatus;
import com.app.entites.type.SubFrequency;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public record SubscriptionResponseDTO(
    @JsonProperty("subscription_id") Long subscriptionId,
    @JsonProperty("create_date") LocalDateTime createDate,
    @JsonProperty("vendor_name") String vendorName,
    @JsonProperty("delivery_date") LocalDate deliveryDate,
    @JsonProperty("payment_method") String paymentMethod,
    SubscriptionStatus status,
    SubFrequency frequency,
    @JsonProperty("delivery_address") Map<String, String> deliveryAddress,
    @JsonProperty("mobile_number") String mobileNumber) {}
