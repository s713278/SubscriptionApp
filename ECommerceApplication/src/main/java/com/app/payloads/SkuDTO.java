package com.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuDTO {

	private Long skuId;

	private String name;

	private String image;

	private String description;

	private Integer quantity;
	private double listPrice;
	private double salePrice;
	/*
	 * private Category category; private Product product;
	 * 
	 * private Store store;
	 */
}
