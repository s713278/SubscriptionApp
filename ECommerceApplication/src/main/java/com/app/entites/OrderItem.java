package com.app.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "sku_id")
  private Sku sku;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  private Integer quantity;

  private double discount;
  private double price;
  private double tax;
  private double unitPrice;
  private double stateTax = 0;
  private double federalTax = 0;

  @Transient private double amount; // quantity * unitPrice + stateTax + federalTax;
}
