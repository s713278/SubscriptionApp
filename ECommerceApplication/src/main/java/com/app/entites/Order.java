package com.app.entites;

import com.app.services.constants.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @OneToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;

    private Double subTotal;
    private Double federalTax;
    private Double stateTax;
    private Double totalAmount;
    
    @Column(name = "order_status", columnDefinition = "order_status_enum")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(
            mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

   
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();
}
