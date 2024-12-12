package com.app.payloads;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public record ProductSkuDTO(@JsonProperty("product_id") Long productId,
                            @JsonProperty("product_name") String productName,
                            @JsonProperty("sku_id") Long skuId,
                            @JsonProperty("image_path") String imagePath,
                            @JsonProperty("sku_name") String skuName,
                            @JsonProperty("sku_size") String skuSize,
                            @JsonProperty("stock") Integer stock,
                            @JsonProperty("sku_type") String type,
                            @JsonProperty("valid_service_days") Integer serviceDays,
                            @JsonProperty("price_id") Long priceId,
                            @JsonProperty("list_price") Double listPrice,
                            @JsonProperty("sale_price") Double salePrice,
                            @JsonProperty("effective_date")LocalDate effectiveDate,
                           // @JsonProperty("sub_frequency")SubFrequency frequency,
                           // @JsonProperty("eligible_delivery_days")Map<String,Object> eligibleDeliveryDays,
                            @JsonProperty("eligible_subscription_options")List<EligibleSubscriptionDTO> eligibleSubscriptionOptions
                            ) implements Serializable {

    @JsonProperty("discount")
    public Double discount() {
        return (listPrice != null && salePrice != null) ? listPrice - salePrice : null;
    }
    @JsonProperty("on_sale")
    public Boolean onSale() {
        return (listPrice != null && salePrice != null) && salePrice < listPrice;
    }
}
