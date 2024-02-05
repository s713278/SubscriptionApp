package com.app.services;

import com.app.payloads.StoreDTO;
import com.app.payloads.response.StoreResponse;

public interface StoreService {

    StoreDTO createStore(StoreDTO category);

    StoreResponse getStore(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    StoreDTO updateStore(StoreDTO storeDTO, Long storeId);

    String deleteStore(Long storeId);
}
