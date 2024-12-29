package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

public record AssignProductsRequest(
    @JsonProperty("product_id") Long productId,
    @JsonProperty("features_map") ObjectNode features) {}
