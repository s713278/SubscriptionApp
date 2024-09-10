package com.app.services.impl;

import com.app.entites.Vendor;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.VendorDTO;
import com.app.payloads.response.ApiResponse;
import com.app.payloads.response.StoreResponse;
import com.app.repositories.StoreRepo;
import com.app.services.VendorService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final ModelMapper modelMapper;

    private final StoreRepo storeRepo;

    @Override
    public ApiResponse<VendorDTO> createStore(VendorDTO storeDTO) {
        Vendor storeEntity = storeRepo.save(modelMapper.map(storeDTO, Vendor.class));
        return ApiResponse.success(modelMapper.map(storeEntity, VendorDTO.class));
    }

    @Override
    public StoreResponse getStore(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Vendor> pageStores = storeRepo.findAll(pageDetails);
        List<Vendor> stores = pageStores.getContent();

        if (stores.size() == 0) {
            throw new APIException("No stores is created till now");
        }

        List<VendorDTO> storeDTOs = stores.stream()
                .map(store -> modelMapper.map(store, VendorDTO.class))
                .collect(Collectors.toList());

        StoreResponse storeResponse = new StoreResponse();
        storeResponse.setContent(storeDTOs);
        storeResponse.setPageNumber(pageStores.getNumber());
        storeResponse.setPageSize(pageStores.getSize());
        storeResponse.setTotalElements(pageStores.getTotalElements());
        storeResponse.setTotalPages(pageStores.getTotalPages());
        storeResponse.setLastPage(pageStores.isLast());

        return storeResponse;
    }

    @Override
    public ApiResponse<VendorDTO> updateStore(VendorDTO storeDTO, Long storeId) {
        Vendor savedStore = storeRepo
                .findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", storeId));
        modelMapper.map(storeDTO, savedStore);
        savedStore.setId(storeId);
        savedStore = storeRepo.save(savedStore);
        return ApiResponse.success(modelMapper.map(savedStore, VendorDTO.class));
    }

    @Override
    public ApiResponse<String> deleteStore(Long storeId) {
        Vendor store = storeRepo
                .findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", storeId));
        storeRepo.deleteById(storeId);
        return ApiResponse.success("Store with id: " + storeId + " deleted successfully !!!");

        // return storeRepo.delete(storeId);
    }
    
    @Override
    public ApiResponse<List<VendorDTO>> getStores() {
        List<Vendor> stores = storeRepo.findAll();
        if (stores.size() == 0) {
            throw new APIException(APIErrorCode.API_404, "No stores is created till now");
        }
        List<VendorDTO> storeDTOs = stores.stream()
                .map(store -> modelMapper.map(store, VendorDTO.class))
                .collect(Collectors.toList());
        return ApiResponse.success(storeDTOs);
    }
}
