package com.app.services;

import com.app.payloads.VendorDTO;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.StoreResponse;
import java.util.List;

public interface VendorService {

    APIResponse<VendorDTO> createStore(VendorDTO category);

    StoreResponse getStore(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    APIResponse<VendorDTO> updateStore(VendorDTO storeDTO, Long storeId);

    APIResponse<String> deleteStore(Long storeId);

    APIResponse<List<VendorDTO>> getStores();
}
