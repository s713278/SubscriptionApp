package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

// @ValidateSubscriptionRequest
@Data
public class CreateSubscriptionRequest {

  @Schema(example = "202")
  @Min(value = 0, message = "Invalid sku id.")
  @NotNull(message = "Sku Id is required.")
  @JsonProperty("sku_id")
  private Long skuId;

  @Schema(example = "8")
  @Min(value = 0, message = "Invalid price id.")
  @NotNull(message = "Price Id is required.")
  @JsonProperty("price_id")
  private Long priceId;

  @Schema(example = "1")
  @Min(value = 1, message = "Invalid quantity.")
  @Max(value = 10, message = "Quantity must be less than or equal to 10")
  @NotNull(message = "Quantity is required")
  private Integer quantity;

  // This can be used for fetching frequency and delivery_mode
  @Schema(example = "9")
  @NotNull(message = "SKU subscription eligible plan is required.")
  @JsonProperty("sku_subscription_plan_id")
  private Long skuSubscriptionPlanId;

  // @NotNull(message = "Delivery date is required")
  // Its required for ONE_TIME type of subscription
  @JsonProperty("delivery_date")
  @Future(message = "Delivery date must be future date")
  private LocalDate deliveryDate;

  // Its required for DAILY/ALTERNATIVE/CUSTOM type of subscriptions
  // @NotNull(message = "Start Date is required")
  @JsonProperty("start_date")
  @Future(message = "Start date must be be future date")
  private LocalDate startDate;

  // @NotNull(message = "End Date is required")
  @JsonProperty("end_date")
  @Future(message = "End date must be be future date")
  private LocalDate endDate;

  // Its required for CUSTOM type of subscription
  @Schema(example = "[1,3,5]")
  @JsonProperty("custom_days")
  private List<Integer> customDays; // Optional: Applicable if frequency is CUSTOM
}
