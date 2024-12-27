package com.app.payloads.request;

import com.app.controllers.validator.ValidateSubscriptionRequest;
import com.app.entites.type.SubFrequency;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@ValidateSubscriptionRequest
@Data
public class CreateSubscriptionRequest {

  @Min(value = 0, message = "Invalid sku id.")
  @NotNull(message = "Sku Id is required.")
  @JsonProperty("sku_id")
  private Long skuId;

  @Min(value = 0, message = "Invalid price id.")
  @NotNull(message = "Price Id is required.")
  @JsonProperty("price_id")
  private Long priceId;

  @Min(value = 1, message = "Invalid quantity.")
  @Max(value = 10, message = "Quantity must be less than or equal to 10")
  @NotNull(message = "Quantity is required")
  private Integer quantity;

  @NotNull(message = "Frequency is required")
  private SubFrequency frequency;

  // @NotNull(message = "Delivery date is required")
  @JsonProperty("delivery_date")
  @Future(message = "Delivery date must be in the future")
  private LocalDate deliveryDate;

  // @NotNull(message = "Start Date is required")
  @JsonProperty("start_date")
  @Future(message = "Start date must be in the future")
  private LocalDate startDate;

  // @NotNull(message = "End Date is required")
  @JsonProperty("end_date")
  @Future(message = "End date must be in the future")
  private LocalDate endDate;

  @JsonProperty("custom_days")
  private List<Integer> customDays; // Optional: Applicable if frequency is CUSTOM
}
