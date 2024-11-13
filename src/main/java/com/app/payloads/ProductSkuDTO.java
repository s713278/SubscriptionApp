package com.app.payloads;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;


public record ProductSkuDTO(@JsonProperty("product_id") Long productId,
                            @JsonProperty("product_name") String productName,
                            @JsonProperty("sku_id") Long skuId,
                            @JsonProperty("image_path") String imagePath,
                            @JsonProperty("sku_name") String skuName,
                            @JsonProperty("sku_size") String skuSize,
                            @JsonProperty("price_id") Long vendorSkuPriceId,
                            @JsonProperty("stock") Integer stock,
                            @JsonProperty("list_price") Double listPrice,
                            @JsonProperty("sale_price") Double salePrice,
                            @JsonProperty("effective_date")LocalDate effectiveDate
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
