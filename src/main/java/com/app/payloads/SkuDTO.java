package com.app.payloads;


import com.app.entites.type.SubFrequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private List<SubFrequency> eligibleFrequency; // For custom
}
