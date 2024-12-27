package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EligibleSubscriptionDTO {
  @JsonProperty("frequency")
  private String frequency;

  @JsonProperty("eligible_delivery_days")
  private Map<String, Object> eligibleDeliveryDays;
}
