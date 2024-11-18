package com.app.entites;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_subscription")
public class Subscription  extends  AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 2038580641079721330L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "customer_id",nullable = false)
    //Foreign column to tb_user table
    private Long userId;

    @Column(name = "sku_id",nullable = false)
    //Foreign column to tb_vendor_sku table
    private Long skuId;

    @Column(name = "price_id",nullable = false)
    //Foreign column to tb_sku_price table
    private Long priceId;

    private Integer quantity;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @JdbcType(value  = PostgreSQLEnumJdbcType.class)
    @Column(name = "frequency", columnDefinition = "subscription_frequency")
    private SubscriptionFrequency frequency; // one_time, daily, weekly, custom

    @JdbcType(value  = PostgreSQLEnumJdbcType.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "subscription_status")
    private SubscriptionStatus status;

    @CollectionTable(name = "tb_subscription_custom_days",joinColumns = @JoinColumn(name = "subscription_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> customDays; // For custom date range
    //1 - Monday

    @Column(name="next_delivery_date")
    private LocalDate nextDeliveryDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "delivery_address", columnDefinition = "jsonb")
    private Map<String, String> deliveryAddress;
    
    @Column(name="update_version",updatable = true)
    private Integer updateVersion=0;

    // Getters and Setters
}
