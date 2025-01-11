package com.app.payloads.response;

import com.app.entites.SubscriptionStatus;
import com.app.entites.type.DeliveryMode;
import com.app.entites.type.SkuType;
import com.app.entites.type.SubFrequency;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscriptionCreateResponse {

  @JsonProperty("subscription_id")
  public Long subscriptionId;

  @JsonProperty("create_date")
  public LocalDateTime createDate;

  @JsonProperty("vendor_name")
  protected String vendorName;

  @JsonProperty("payment_method")
  protected String paymentMethod;

  @JsonProperty("subscription_status")
  protected SubscriptionStatus status;

  @JsonProperty("frequency")
  protected SubFrequency frequency;

  @JsonProperty("delivery_mode")
  protected DeliveryMode deliveryMode;

  @JsonProperty("mobile_number")
  protected String mobileNumber;

  @JsonProperty("item_type")
  protected SkuType skuType;
}
