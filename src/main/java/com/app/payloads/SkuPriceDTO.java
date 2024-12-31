package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SkuPriceDTO implements Serializable {
  @FutureOrPresent(message = "Price effective date should be today or future.")
  @NotBlank(message = "Effective date is required.")
  @JsonProperty("effective_date")
  private LocalDate effectiveDate;

  @Schema(example = "100")
  @JsonProperty("list_price")
  private BigDecimal listPrice;

  @Schema(example = "90")
  @JsonProperty("sale_price")
  private BigDecimal salePrice;
}
