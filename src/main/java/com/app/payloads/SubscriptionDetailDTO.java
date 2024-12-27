package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionDetailDTO(
    Long subscriptionId,
    Long userId,
    Long vendorId,
    String vendorName,
    BigDecimal listPrice,
    BigDecimal salePrice,
    Integer quantity,
    BigDecimal amount,
    String skuName,
    LocalDate startDate,
    String frequency,
    String status,
    LocalDate nextDeliveryDate,
    String name,
    String imagePath) {
  @JsonProperty("discount")
  public Double discount() {
    return (listPrice != null && salePrice != null)
        ? listPrice.subtract(salePrice).doubleValue()
        : null;
  }

  @JsonProperty("on_sale")
  public Boolean onSale() {
    return (listPrice != null && salePrice != null)
        && salePrice.doubleValue() < listPrice.doubleValue();
  }
}
