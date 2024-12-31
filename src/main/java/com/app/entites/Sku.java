package com.app.entites;

import com.app.entites.type.SkuType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tb_sku")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sku extends AbstractAuditingEntity<Long> implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank
  @Size(min = 3, message = "Name must contain at least 3 characters")
  private String name;

  private String description;

  @Column(name = "type", columnDefinition = "sku_type")
  @JdbcType(value = PostgreSQLEnumJdbcType.class)
  @Enumerated(EnumType.STRING)
  private SkuType skuType; // Indicates if this SKU is an ITEM or SERVICE

  private String imagePath;

  /*@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "vendor_product_id")
  private VendorProduct vendorProduct;*/
  @NotNull
  @Column(name = "vendor_product_id")
  private Long vendorProductId;

  @Column(name = "sku_code")
  private String skuCode;

  private String weight; // 500 Grams, 1 Service,5 Service or 5 Sessions

  private String dimension;

  private Integer stock;

  @Column(name = "is_active")
  private boolean active = true;

  @Column(name = "subscription_eligible")
  private boolean subscriptionEligible;

  @Column(name = "cancel_eligible")
  private boolean cancelEligible;

  @Column(name = "return_eligible")
  private boolean returnEligible;

  @OneToOne(
      mappedBy = "sku",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY) // Sku is the "owner" of the relationship
  private ServiceAttribute serviceAttributes;

  @OneToMany(
      mappedBy = "sku",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY) // Sku is the "owner" of the relationship
  List<SkuPrice> priceList;

  @OneToMany(
      mappedBy = "sku",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<SkuSubscriptionPlan> eligibleSubPlans;
}
