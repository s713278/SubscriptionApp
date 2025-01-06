package com.app.payloads;

import com.app.entites.type.SkuType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
public class SkuDTO {
  @JsonProperty("vendor_product_id")
  private Long vendorProductId;

  @JsonProperty("sku_id")
  private Long skuId;

  @JsonProperty("sku_name")
  private String skuName;

  @JsonProperty("image_path")
  private String imagePath;

  @JsonProperty("sku_size")
  String skuSize;

  @JsonProperty("sku_type")
  private SkuType skuType;

  @JsonProperty("is_active")
  private Boolean active;

  @JsonProperty("valid_days")
  private Integer validDays;

  @JsonProperty("price_id")
  private Long priceId;

  @JsonProperty("list_price")
  private Double listPrice;

  @JsonProperty("sale_price")
  private Double salePrice;

  @JsonProperty("effective_date")
  private LocalDate effectiveDate;

  @JsonProperty("eligible_subscription_details")
  private List<SubscriptionDetails> eligibleSubscriptionDetails;

  // Getters and setters...
  @Getter
  @Setter
  @NoArgsConstructor
  public static class SubscriptionDetails implements Serializable {
    private Long id;
    private String frequency;
    private List<String> eligibleDeliveryDays;
    // Getters and setters...
  }
}
