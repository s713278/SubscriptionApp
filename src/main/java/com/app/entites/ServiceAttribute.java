package com.app.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_service_attributes")
public class ServiceAttribute {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "valid_days")
  private Integer validDays;

  @NotNull
  @Column(name = "no_of_uses")
  private Integer noOfUses;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "sku_id", unique = true) // Foreign key in tb_service_attributes table
  private Sku sku;
}
