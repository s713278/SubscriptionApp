package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

public record AssignCategoriesRequest(
    @Schema(example = "[100]") @JsonProperty("category_ids") Set<Long> categoryIds) {}
