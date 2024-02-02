package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

	@JsonProperty("item_id")
	private Long cartItemId;
	//private CartDTO cart;
	
	@JsonIgnore
	private SkuDTO sku;
	//private ProductDTO product;
	private Integer quantity;
	private double discount;
	private double unitPrice;
	
	@JsonIgnore
	private double stateTax = 0;
	
	@JsonIgnore
	private double federalTax = 0;
	
	@JsonProperty("item_amount")
	private double amount ; // quantity * unitPrice + stateTax + federalTax;
	
	@JsonProperty("item_name")
	private String skuName;
	
	public String getSkuName() {
		return sku.getName();
	}
	
}
