package com.app.entites;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  private Instant orderTime;

  private String email;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "user_id")
  private User user;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "payment_id")
  private Payment payment;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "shipping_id")
  private Shipping shipping;

  private Double subTotal;
  private Double federalTax;
  private Double stateTax;
  private Double totalAmount;
  private String orderStatus;

  @OneToMany(
      mappedBy = "order",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  @OneToOne
  @JoinColumn(name = "store_id")
  private Store store;
}
