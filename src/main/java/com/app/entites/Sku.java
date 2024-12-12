package com.app.entites;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import com.app.entites.type.SkuType;
import com.app.entites.type.SubFrequency;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
  
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name="sku_code")
    private String skuCode;

    private String size; //500 Grams, 1 Service,5 Service or 5 Sessions

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    private Integer stock;

   // private LocalDate effectiveDate;//Start date when this price is effective
    private Integer serviceValidDays; //This is primarily for SERVICE type skus and Determine how many days the service is active.

    //@CollectionTable(name = "tb_sku_sub_plan",joinColumns = @JoinColumn(name = "sku_id"))
    //@Enumerated(EnumType.STRING)
    //@ElementCollection(fetch = FetchType.EAGER)
    @Transient
    private List<SubFrequency> eligibleSubPlan; // For custom date range

    @Column(name = "available")
    private boolean available = true;

    @Column(name = "subscription_eligible")
    private boolean subscriptionEligible = false;

    @Column(name = "cancel_eligible")
    private boolean cancelEligible;

    @Column(name = "return_eligible")
    private boolean returnEligible;

    @OneToMany(mappedBy = "sku", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<SkuSubscription> eligibleSubPlans;

}
