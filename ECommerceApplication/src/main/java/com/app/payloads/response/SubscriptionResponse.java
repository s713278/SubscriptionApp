package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public record SubscriptionResponse(boolean success,List<Data> subscribedtItems) {


    public static record Data(@JsonProperty("order_id") Long id, LocalDateTime nextDeliveryDate, String message) {

     }
}
