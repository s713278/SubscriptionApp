package com.app.entites;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Table(name = "tb_vendor_sku_price")
@Data
@Entity
public class VendorSkuPrice extends AbstractAuditingEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

    private Integer stock;

    private BigDecimal listPrice;
    private BigDecimal salePrice;
    private BigDecimal processingFee;

    private LocalDate effectiveDate;

    @CollectionTable(name = "tb_sku_eligible_frequency",joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<SubscriptionFrequency> eligibleFrequency; // For custom date range
    // Getters and Setters
}
