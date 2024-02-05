package com.app.payloads.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequest {
    @Schema(description = "User ID", example = "1")
    @NotNull(message = "User ID") private Long userId;

    @Schema(description = "SKU ID", example = "1")
    @NotNull(message = "Sku is required.") private Long skuId;

    @Schema(description = "Quantity", example = "1")
    @Min(1)
    @Max(25)
    private Integer quantity;
}
