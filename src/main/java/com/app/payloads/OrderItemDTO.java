package com.app.payloads;

import com.app.payloads.request.SkuCreateRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

  @JsonIgnore private Long id;

  private Integer quantity;

  @JsonProperty("unit_price")
  private double unitPrice;

  private double discount;

  @JsonIgnore private SkuCreateRequest sku;

  @JsonProperty("state_tax")
  private double stateTax = 0;

  @JsonProperty("federal_tax")
  private double federalTax = 0;

  @Transient private double amount; // quantity * unitPrice + stateTax + federalTax;
}
