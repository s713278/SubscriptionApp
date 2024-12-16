package com.app.payloads.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;



public record VendorListingResponse<T> (
        @JsonProperty("result")
        List<T> content,

    @JsonProperty("page_number")
     Integer pageNumber,

     @JsonProperty("page_size")
     Integer pageSize,

     @JsonProperty("total_elements")
     Long totalElements,

     @JsonProperty("total_pages")
     Integer totalPages,

     @JsonProperty("last_page")
     boolean lastPage
){

}
