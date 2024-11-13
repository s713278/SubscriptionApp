package com.app.payloads;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.app.entites.SubscriptionFrequency;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuDTO implements Serializable {
    private Long id;
    private String name;
    private String imagePath;
    private String description;
    private String size;
    private String skuCode;
    private BigDecimal listPrice;
    private BigDecimal salePrice;
    private BigDecimal processingFee;
    private BigDecimal shippingPrice;
    private LocalDate effectiveDate;//Start date when this price is effective
    private Integer serviceValidDays; //This is primarily for SERVICE type skus and Determine how many days the service is active.
    private List<SubscriptionFrequency> eligibleFrequency; // For custom
}
