package com.app.entites;

import java.time.LocalDateTime;

import com.app.services.constants.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
