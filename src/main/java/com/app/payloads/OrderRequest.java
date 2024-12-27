package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderRequest {

  @JsonProperty("user_id")
  private Long userId;

  @JsonProperty("cart_id")
  private Long cartId;

  @JsonProperty("payment_details")
  private PaymentDTO paymentDetails;

  @JsonProperty("shipping_details")
  private ShippingDTO shippingDetails;
}
