package com.app.repositories.projections;

import com.app.entites.type.SkuType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public interface SkuProjection {
  @JsonProperty("vendor_product_id")
  Long getVendorProductId();

  @JsonProperty("sku_id")
  Long getSkuId();

  @JsonProperty("name")
  String getSkuName();

  @JsonProperty("image_path")
  String getImagePath();

  @JsonProperty("size")
  String getSkuSize();

  @JsonProperty("type")
  SkuType getSkuType();

  @JsonProperty("is_active")
  Boolean getActive();

  @JsonProperty("valid_days")
  String getValidDays();

  @JsonProperty("price_id")
  Long getPriceId();

  @JsonProperty("list_price")
  Double getLatestListPrice();

  @JsonProperty("sale_price")
  Double getLatestSalePrice();

  @JsonProperty("latest_effective_date")
  LocalDate getLatestEffectiveDate();

  // List<SubscriptionDetails> getEligibleSubscriptionDetails();

  interface SubscriptionDetails {
    Long getId(); // Matches 'id' in JSON_BUILD_OBJECT

    String getFrequency(); // Matches 'frequency'

    List<String> getEligibleDeliveryDays(); // Matches 'eligible_delivery_days'
  }
}
