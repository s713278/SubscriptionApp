package com.app.entites;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_subscription")
public class Subscription  implements Serializable {
    @Serial
    private static final long serialVersionUID = 2038580641079721330L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id")
    private Sku sku;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @JdbcType(value  = PostgreSQLEnumJdbcType.class)
    @Column(name = "frequency", columnDefinition = "subscription_frequency")
    private SubscriptionFrequency frequency; // one_time, daily, weekly, custom

    @JdbcType(value  = PostgreSQLEnumJdbcType.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "subscription_status")
    private SubscriptionStatus status;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> customDays; // For custom date range
    //1 - Monday

    @Column(name="from_start_date")
    private LocalDate fromStartDate;
    
    @Column(name="next_delivery_date")
    private LocalDate nextDeliveryDate;
    

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "delivery_address", columnDefinition = "jsonb")
    private Map<String, String> deliveryAddress;
    
    @Column(name="update_version",updatable = true)
    private Integer updateVersion=0;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date",updatable = false, nullable = false)
    private LocalDateTime lastModifiedDate;

    // Getters and Setters
}
