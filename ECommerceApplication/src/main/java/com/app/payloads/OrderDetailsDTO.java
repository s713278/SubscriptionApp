package com.app.payloads;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.app.entites.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class OrderDetailsDTO implements Serializable {
    private Long orderId;
    private LocalDate deliveryDate;
    private String vendorName;
    private String itemName;
    private String size;
    private Integer quantity;
    private Double unitPrice;
    private BigDecimal amount ; // quantity*amount
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscount;

    public OrderDetailsDTO(Order order){
        this.orderId=order.getId();
        this.deliveryDate = order.getDeliveryDate();
       // this.amount = order.getTotalAmount();
        this.quantity = order.getQuantity();
    }
}
