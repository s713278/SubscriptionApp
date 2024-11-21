package com.app.entites;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_sku_sub_plan")
public class SkuSubscription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

    @ManyToOne
    @JoinColumn(name = "sub_plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "eligible_delivery_days", columnDefinition = "jsonb")
    private Map<String, Object> eligibleDeliveryDays;

    // Additional fields can be added as needed
  //  @Column(name = "validity")
    //private String validity;
}
