package com.app.payloads;

import com.app.entites.Product;
import com.app.entites.Vendor;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuDTO {

    private Long id;
    private String name;
    private String imagePath;
    private String description;
    private Integer quantity;
    private double listPrice;
    private double salePrice;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Vendor store;
}
