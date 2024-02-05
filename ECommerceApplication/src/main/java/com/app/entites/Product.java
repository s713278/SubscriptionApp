package com.app.entites;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product name must contain atleast 3 characters")
    private String productName;

    private String image;

    @NotBlank
    @Size(min = 25, message = "Product description must contain atleast 25 characters")
    private String description;

    /*
     * private Integer quantity; private double price; private double discount;
     * private double specialPrice;
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Sku> skus;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private ObjectNode features;
}
