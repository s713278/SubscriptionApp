package com.app.entites;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
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

        private LocalDate startDate;
        private LocalDate endDate;
        
        @Enumerated(EnumType.STRING)
        private SubscriptionStatus status;
        
        @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
        private List<SubscriptionItem> subscriptionItems;

    // Getters and Setters
}
