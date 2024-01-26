package com.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

	private Long cartItemId;
	private CartDTO cart;
	private SkuDTO sku;
	private ProductDTO product;
	private Integer quantity;
	private double discount;
	private double productPrice;
}
