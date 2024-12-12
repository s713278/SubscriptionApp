package com.app.entites;

import java.time.LocalDateTime;

import com.app.entites.type.OrderStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_order_status_history")
public class OrderStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private LocalDateTime changedAt = LocalDateTime.now();

    private String changedBy;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
