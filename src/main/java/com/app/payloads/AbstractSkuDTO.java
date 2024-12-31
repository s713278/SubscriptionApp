package com.app.payloads;

import com.app.entites.type.SkuType;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AbstractSkuDTO implements Serializable {

  @NotBlank(message = "Name is required.")
  @Schema(example = "NON GMO Tomato")
  @JsonProperty("name")
  private String name;

  @Schema(example = "Organic and Own farm grown vegetables")
  private String description;

  @JsonEnumDefaultValue
  @Schema(example = "ITEM or SERVICE")
  @JsonProperty(value = "sku_type", defaultValue = "ITEM")
  private SkuType skuType; // Indicates if this SKU is an ITEM or SERVICE

  @Schema(example = "http://example.com/fruits/orange_1.png")
  @JsonProperty("image_path")
  private String imagePath;

  @JsonIgnore
  @JsonProperty("sku_code")
  private String skuCode; // Thinking to generate at backend Vendor_Product_SkuID

  @Schema(example = "1 Kg")
  private String weight; // 500 Grams, 1 Service,5 Service or 5 Sessions

  @Schema(example = "10*12*8")
  private String dimension;

  @JsonIgnore private Integer stock;

  private boolean active = true;

  @JsonProperty("subscription_eligible")
  private boolean subscriptionEligible;

  @JsonProperty("cancel_eligible")
  private boolean cancelEligible;

  @JsonProperty("return_eligible")
  private boolean returnEligible;

  @JsonProperty("service_attributes")
  private ServiceAttributeDTO serviceAttributes;

  @JsonProperty("price_list")
  List<SkuPriceDTO> priceList;

  @JsonProperty("eligible_sub_plans")
  private List<SubscriptionPlanDTO> eligibleSubPlans;
}
