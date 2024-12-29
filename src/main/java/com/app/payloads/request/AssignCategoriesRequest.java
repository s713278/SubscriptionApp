package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;

public record AssignCategoriesRequest(@JsonProperty("category_ids") Set<Long> categoryIds) {}
