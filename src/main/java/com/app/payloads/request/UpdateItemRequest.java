package com.app.payloads.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateItemRequest {

  @NotBlank(message = "Order is required.")
  private String orderId;

  @NotBlank(message = "Store is required.")
  private String storeId;

  @NotBlank(message = "Category is required.")
  private String catId;

  @NotBlank(message = "Sku is required.")
  private String skuId;

  @Min(1)
  @Max(25)
  private Integer quantity;
}
