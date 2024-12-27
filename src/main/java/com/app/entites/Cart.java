package com.app.entites;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_cart")
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Customer user;

  @OneToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @JoinColumn(name = "cart_id")
  private List<CartItem> cartItems = new ArrayList<>();

  private BigDecimal totalPrice;
}
