package com.app.entites;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_product_vendor")
public class ProductVendor implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne
  @JoinColumn(name = "vendor_id", nullable = false)
  private Vendor vendor;

  private ObjectNode features;
}
