package com.app.entites;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Skus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sku {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long skuId;

	@NotBlank
	@Size(min = 3, message = "Sku name must contain atleast 3 characters")
	private String name;

	private String image;

	@NotBlank
	@Size(min = 25, message = "Product description must contain atleast 25 characters")
	private String description;

	private Integer quantity;
	private double listPrice;
	private double salePrice;

	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;

	/*
	 * @OneToMany(mappedBy = "sku", cascade = { CascadeType.PERSIST,
	 * CascadeType.MERGE }, fetch = FetchType.EAGER) private List<CartItem> skus =
	 * new ArrayList<>();
	 * 
	 * @OneToMany(mappedBy = "sku", cascade = { CascadeType.PERSIST,
	 * CascadeType.MERGE }) private List<OrderItem> orderItems = new ArrayList<>();
	 */

}
