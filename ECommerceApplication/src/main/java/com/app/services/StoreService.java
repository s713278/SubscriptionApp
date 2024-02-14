package com.app.services;

import com.app.payloads.StoreDTO;
import com.app.payloads.response.ApiResponse;
import com.app.payloads.response.StoreResponse;
import java.util.List;

public interface StoreService {

    ApiResponse<StoreDTO> createStore(StoreDTO category);

    StoreResponse getStore(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ApiResponse<StoreDTO> updateStore(StoreDTO storeDTO, Long storeId);

    ApiResponse<String> deleteStore(Long storeId);

    ApiResponse<List<StoreDTO>> getStores();
}
