package com.app.entites;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_sku_price")
public class SkuPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "sku_id", nullable = false)
  private Sku sku;

  @Column(name = "list_price", nullable = false)
  private BigDecimal listPrice;

  @Column(name = "sale_price", nullable = false)
  private BigDecimal salePrice;

  @Column(name = "effective_date", nullable = false)
  private LocalDate effectiveDate;

  @Column(name = "created_by")
  private String updatedBy;

  // Getters and Setters
}
