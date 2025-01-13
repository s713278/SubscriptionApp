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

  @JsonProperty("discount")
  private Double discount;

  @JsonProperty("on_sale")
  private Boolean onSale;

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

  public void setDiscount() {
    this.discount = (this.listPrice != null && salePrice != null) ? listPrice - salePrice : null;
  }

  public void setOnSale() {
    this.onSale = (listPrice != null && salePrice != null) && salePrice < listPrice;
  }
}
