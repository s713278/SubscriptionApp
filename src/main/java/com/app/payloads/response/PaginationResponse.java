package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PaginationResponse<T>(
    @JsonProperty("result") List<T> content,
    @JsonProperty("page_number") Integer pageNumber,
    @JsonProperty("page_size") Integer pageSize,
    @JsonProperty("total_elements") Long totalElements,
    @JsonProperty("total_pages") Integer totalPages,
    @JsonProperty("last_page") boolean lastPage) {}
