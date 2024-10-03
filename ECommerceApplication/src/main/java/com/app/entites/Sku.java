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
@Table(name = "tb_sku")
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
  
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    private String skuCode;
    
    private String size;
    
   // private Integer quantity;
    private double listPrice;
    private double salePrice;
    
    private Integer stock;

}
