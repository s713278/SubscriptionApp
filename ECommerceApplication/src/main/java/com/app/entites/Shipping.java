package com.app.entites;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "Shipping")
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(
      mappedBy = "shipping",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Order order;

  @NotBlank
  @Size(min = 4, message = "Shipping method must contain atleast 4 characters")
  private String shippingMethod;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "shipping_id")
  private Address address;
}
