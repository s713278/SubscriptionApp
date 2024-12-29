package com.app.entites;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;

@Getter
@Entity
@Table(name = "tb_category_vendor")
public class VendorCategory /* extends AbstractAuditingEntity<Long>*/ implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // @ManyToOne
  // @JoinColumn(name = "category_id", nullable = false)
  private Long categoryId;

  // @ManyToOne
  // @JoinColumn(name = "vendor_id", nullable = false)
  // private Vendor vendor;
  private Long vendorId;

  public VendorCategory() {}

  public VendorCategory(Long categoryId, Long vendorId) {
    this.categoryId = categoryId;
    this.vendorId = vendorId;
  }
}
