package com.app.payloads;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

	@JsonProperty("cart_id")
	private Long cartId;
	
	@JsonProperty("cart_total")
	private Double totalPrice = 0.0;
	//private List<SkuDTO> skus = new ArrayList<>();
	
	@JsonProperty("cart_items")
	private List<CartItemDTO> items = new ArrayList<>();
	
	@JsonProperty("items_count")
	private Integer itemsCount;
	
	public Integer getItemsCount() {
		return items.stream().map(t -> t.getQuantity()).mapToInt(Integer::intValue).sum();
	}
}
