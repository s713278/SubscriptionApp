package com.app.payloads;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EligibleSubscriptionDTO {
    @JsonProperty("frequency")
    private String frequency;
    @JsonProperty("eligible_delivery_days")
    private Map<String,Object> eligibleDeliveryDays;
}
