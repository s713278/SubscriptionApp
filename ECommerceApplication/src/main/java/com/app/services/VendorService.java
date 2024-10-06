package com.app.services;

import java.util.List;

import com.app.payloads.VendorDTO;
import com.app.payloads.response.AppResponse;
import com.app.payloads.response.StoreResponse;

public interface VendorService {

    AppResponse<VendorDTO> createStore(VendorDTO category);

    StoreResponse getStore(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    AppResponse<VendorDTO> updateStore(VendorDTO storeDTO, Long storeId);

    AppResponse<String> deleteStore(Long storeId);

    AppResponse<List<VendorDTO>> getStores();
}
