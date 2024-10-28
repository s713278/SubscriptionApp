package com.app.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.constants.CacheType;
import com.app.entites.Vendor;
import com.app.entites.type.VendorStatus;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.VendorDTO;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.StoreResponse;
import com.app.repositories.RepositoryManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VendorService  {

    private final ModelMapper modelMapper;
    private final RepositoryManager repoManager;

    @Transactional
    public APIResponse<VendorDTO> createVendor(VendorDTO storeDTO) {
        Vendor storeEntity = repoManager.getVendorRepo().save(modelMapper.map(storeDTO, Vendor.class));
        return APIResponse.success(HttpStatus.CREATED.value(), modelMapper.map(storeEntity, VendorDTO.class));
    }

    public StoreResponse fetchAllVendors(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Vendor> pageStores =  repoManager.getVendorRepo().findAll(pageDetails);
        List<Vendor> stores = pageStores.getContent();

        if (stores.size() == 0) {
            throw new APIException(APIErrorCode.API_400,"No stores is created!!");
        }

        List<VendorDTO> storeDTOs = stores.stream().map(store -> modelMapper.map(store, VendorDTO.class))
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

    @Transactional
    public APIResponse<VendorDTO> updateStore(VendorDTO storeDTO, Long storeId) {
        Vendor savedStore =  repoManager.getVendorRepo().findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", storeId));
        modelMapper.map(storeDTO, savedStore);
        savedStore.setId(storeId);
        savedStore =  repoManager.getVendorRepo().save(savedStore);
        return APIResponse.success(HttpStatus.OK.value(), modelMapper.map(savedStore, VendorDTO.class));
    }

    @Transactional
    public APIResponse<String> deleteVendor(Long vendorId) {
        Vendor store =  repoManager.getVendorRepo().findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", vendorId));
        repoManager.getVendorRepo().deleteById(vendorId);
        return APIResponse.success(HttpStatus.OK.value(), "Store with id: " + vendorId + " deleted successfully !!!");

        // return storeRepo.delete(storeId);
    }

    public List<VendorDTO> fetchAllVendors() {
        List<Vendor> stores =  repoManager.getVendorRepo().findAll();
        if(stores.isEmpty()){
            return List.of();
        }
        return stores.stream().map(store -> modelMapper.map(store, VendorDTO.class))
                .collect(Collectors.toList());
    }


    @Cacheable(value = CacheType.CACHE_TYPE_VENDORS,key = "#status")
    public List<Vendor> fetchVendorsByStatus(final String status) {
        var statusEnum = VendorStatus.valueFromString(status.toUpperCase());
        return  repoManager.getVendorRepo().findAllByStatus(statusEnum);
    }


    @Cacheable(value = CacheType.CACHE_TYPE_VENDORS,key = "#vendorId")
    public VendorDTO fetchVendorById(Long vendorId) {
        var vendor =  repoManager.getVendorRepo().findById(vendorId)
                .orElseThrow(()-> new APIException(APIErrorCode.API_404,"Vendor not existed!!"));
        return modelMapper.map(vendor,VendorDTO.class);
    }

    @Cacheable(value =CacheType.CACHE_TYPE_VENDORS,key = "#serviceArea")
    public List<VendorDTO> fetchVendorsByServiceArea(String serviceArea) {
       var vendorsList =   repoManager.getVendorRepo().findByServiceArea(serviceArea);
        return vendorsList.stream().map(vendor->modelMapper.map(vendor,VendorDTO.class)).toList();
    }

    public Vendor fetchVendor(final Long vendorId){
        return repoManager.getVendorRepo().findById(vendorId).orElseThrow(() -> new APIException(APIErrorCode.API_400, "Vendor not exited in the system."));

    }

}
