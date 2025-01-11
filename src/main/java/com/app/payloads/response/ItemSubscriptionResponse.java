package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSubscriptionResponse extends SubscriptionCreateResponse {

  @JsonProperty("quantity")
  private Integer quantity;

  @JsonProperty("next_delivery_date")
  private LocalDate nextDeliveryDate;

  @JsonProperty("delivery_address")
  private Map<String, String> deliveryAddress;
}
