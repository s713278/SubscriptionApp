package com.app.entites;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "sku_id")
    private Sku sku;

    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", columnDefinition = "subscription_frequency")
    private SubscriptionFrequency frequency; // one_time, daily, weekly, custom

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "subscription_status")
    private SubscriptionStatus status;

    @ElementCollection
    private List<Integer> customDays; // For custom date range
    //1 - Monday

    private LocalDate fromStartDate;
    
    private LocalDate nextDeliveryDate;

    @CreatedDate
    @Column(name = "created_date", updatable = false, insertable = false)
    private Instant createdDate = Instant.now();

    @LastModifiedDate
    @Column(name = "last_modified_date",updatable = false, insertable = false)
    private Instant lastModifiedDate = Instant.now();

    // Getters and Setters
}
