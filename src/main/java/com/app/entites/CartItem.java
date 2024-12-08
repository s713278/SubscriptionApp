package com.app.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "sku_id")
    private Sku sku;

    private Integer quantity;
    private double discount;
    private double unitPrice;
    /*
     * private double stateTax = 0; private double federalTax = 0;
     */

    @Transient
    private double amount; // quantity * unitPrice + stateTax + federalTax;
}
