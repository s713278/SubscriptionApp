package com.app.entites;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import com.app.entites.type.OrderStatus;
import com.app.entites.type.PaymentStatus;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_order")
@Data
@NoArgsConstructor
public class Order extends AbstractAuditingEntity<Long> {
    @Serial
    private static final long serialVersionUID = 1529689654420291266L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vendorId;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE },fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    
    private BigDecimal price;
    private int quantity;
    private BigDecimal centralTax;
    private BigDecimal stateTax;
    
    @Transient
    private double totalAmount;

    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Column(name = "order_status", columnDefinition = "order_status_enum")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Column(name = "payment_status", columnDefinition = "payment_status_enum")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    

    /*@OneToMany(mappedBy = "order", cascade = { CascadeType.PERSIST,
            CascadeType.MERGE }, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();*/

    @Override
    public Long getId() {
        return id;
    }
}