package com.app.services;

import com.app.payloads.VendorDTO;
import com.app.payloads.response.ApiResponse;
import com.app.payloads.response.StoreResponse;
import java.util.List;

public interface VendorService {

    ApiResponse<VendorDTO> createStore(VendorDTO category);

    StoreResponse getStore(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ApiResponse<VendorDTO> updateStore(VendorDTO storeDTO, Long storeId);

    ApiResponse<String> deleteStore(Long storeId);

    ApiResponse<List<VendorDTO>> getStores();
}
