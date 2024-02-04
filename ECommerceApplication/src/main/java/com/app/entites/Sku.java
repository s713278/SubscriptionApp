package com.app.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  private Long id;

  @NotBlank
  @Size(min = 3, message = "Name must contain atleast 3 characters")
  private String name;

  private String imagePath;

  @NotBlank
  @Size(min = 25, message = "Description must contain atleast 25 characters")
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
}
