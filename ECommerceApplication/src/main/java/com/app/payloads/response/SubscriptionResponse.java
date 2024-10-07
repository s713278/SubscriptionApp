package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record SubscriptionResponse(boolean success, String message, Data data) {

    // Constructor with only 'success' and 'message', setting 'data' as null
    public SubscriptionResponse(boolean success, String message) {
        this(success, message, null); // 'data' is set to null by default
    }

    public static record Data(@JsonProperty("subcription_id") Long id,
            @JsonProperty("next_delivery_date") LocalDate nextDeliveryDate) {

    }
}
