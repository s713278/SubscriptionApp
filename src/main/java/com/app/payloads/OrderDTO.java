package com.app.payloads;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.app.entites.Vendor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @JsonProperty("order_id")
    private Long orderId;

    private String email;

    @JsonProperty("order_date")
    private Instant orderTime;

    @JsonProperty("store_name")
    private String storeName;

    @JsonProperty("order_items")
    private List<OrderItemDTO> items = new ArrayList<>();

    @JsonProperty("payment_details")
    private PaymentDTO payment;

    @JsonProperty("total_amount")
    private Double totalAmount;

    @JsonProperty("order_status")
    private String orderStatus;

    @JsonProperty("shipping_details")
    private ShippingDTO shipping;

    @JsonProperty("sub_total")
    private Double subTotal;

    @JsonProperty("federal_tax")
    private Double federalTax;

    @JsonProperty("state_tax")
    private Double stateTax;

    @JsonIgnore
    private Vendor store;

    @JsonProperty("store_name")
    public String getStoreName() {
        return store.getBusinessName();
    }
}
