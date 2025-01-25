package com.app.payloads;

import com.app.entites.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class OrderDetailsDTO implements Serializable {
  @JsonProperty("order_id")
  private Long orderId;

  @JsonProperty("delivery_date")
  private LocalDate deliveryDate;

  @JsonProperty("vendor_name")
  private String vendorName;

  @JsonProperty("sku_name")
  private String itemName;

  @JsonProperty("size")
  private String size;

  @JsonProperty("quantity")
  private Integer quantity;

  @JsonProperty("unit_price")
  private Double unitPrice;

  @JsonProperty("amount")
  private BigDecimal amount; // quantity*amount

  @JsonProperty("discount")
  private BigDecimal discount;

  @JsonProperty("total_amount") // Including Tax
  private BigDecimal totalAmount;

  @JsonProperty("total_discount")
  private BigDecimal totalDiscount;

  public OrderDetailsDTO(Order order) {
    this.orderId = order.getId();
    this.deliveryDate = order.getDeliveryDate();
    // this.amount = order.getTotalAmount();
    this.quantity = order.getQuantity();
  }
}
