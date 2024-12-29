package com.app.entites;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "tb_product_vendor")
public class VendorProduct implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /*@ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;


  @ManyToOne
  @JoinColumn(name = "vendor_id", nullable = false)
  private Vendor vendor;
   */
  @Column(name = "product_id")
  private Long productId;

  @Column(name = "vendor_id")
  private Long vendorId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "eligible_delivery_days", columnDefinition = "jsonb")
  private ObjectNode features;
}
