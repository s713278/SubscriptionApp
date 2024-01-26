package com.app.payloads.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddItemRequest {

	@NotBlank(message = "Category is required.")
	private Long catId;

	@NotBlank(message = "Sku is required.")
	private Long skuId;

	@Min(1)
	@Max(25)
	private Integer quantity;

}
