package com.app.entites;

import com.app.services.constants.ShippingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tb_shipping")
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Transient
  //  @OneToOne(mappedBy = "shipping", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  private Order order;

  // @NotBlank
  // @Size(min = 4, message = "Shipping method must contain atleast 4 characters")
  @Column(name = "shipping_method", columnDefinition = "ship_type_enum")
  @Enumerated(EnumType.STRING)
  private ShippingType shippingMethod;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "shipping_id")
  private Address address;
}
