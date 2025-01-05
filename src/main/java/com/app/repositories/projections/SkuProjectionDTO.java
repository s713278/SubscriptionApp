package com.app.repositories.projections;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class SkuProjectionDTO {
  Long vendorProductId;
  Long skuId;
  String skuName;
  String imagePath;
  String skuSize;
  String skuType;
  Boolean active;
  String validDays;
  Long priceId;
  Double latestListPrice;
  Double latestSalePrice;
  LocalDate latestEffectiveDate;
  List<SubscriptionDetailsDTO> eligibleSubscriptionDetails;

  @Data
  static class SubscriptionDetailsDTO {
    Long id; // Matches 'id' in JSON_BUILD_OBJECT
    String frequency; // Matches 'frequency'
    List<String> eligibleDeliveryDays; // Matches 'eligible_delivery_days'
  }
}
