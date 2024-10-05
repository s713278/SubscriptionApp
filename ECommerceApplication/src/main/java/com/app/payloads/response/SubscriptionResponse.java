package com.app.payloads.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SubscriptionResponse(boolean success,Data subscribedtItem) {

    public static record Data(@JsonProperty("subcription_id") Long id, LocalDate nextDeliveryDate) {

     }
}
