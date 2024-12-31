package com.app.payloads;

import com.app.entites.type.DeliveryMode;
import com.app.entites.type.SubFrequency;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscriptionPlanDTO implements Serializable {

  @Schema(example = "1")
  @NotBlank(message = "Subscription plan is required.")
  @JsonProperty(value = "sub_plan_id")
  private Long subPlanId;

  @JsonProperty(value = "sub_frequency")
  private SubFrequency frequency;

  @JsonProperty(value = "delivery_mode")
  private DeliveryMode deliveryMode;

  @Schema(example = "{ \"delivery_days\": [\"SUNDAY\", \"WEDNESDAY\"] }")
  @JsonProperty(value = "eligible_delivery_days")
  private Map<String, Object> eligibleDeliveryDays;
}
