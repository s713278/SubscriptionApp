package com.app.payloads;

import com.app.entites.Category;
import com.app.entites.Product;
import com.app.entites.Store;

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

	private Category category;
	private Product product;

	private Store store;

}
