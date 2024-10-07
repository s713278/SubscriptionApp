package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    @JsonProperty("cart_id")
    private Long cartId;

    @JsonProperty("total_amount")
    private Double totalPrice = 0.0;

    // private List<SkuDTO> skus = new ArrayList<>();

    @JsonProperty("items")
    private List<CartItemDTO> items = new ArrayList<>();

    @JsonProperty("items_count")
    private Integer itemsCount;

    public Integer getItemsCount() {
        return items.stream().map(t -> t.getQuantity()).mapToInt(Integer::intValue).sum();
    }
}
