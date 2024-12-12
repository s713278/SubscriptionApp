package com.app.services.impl;

import java.util.*;
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
import com.app.payloads.VendorBasicDTO;
import com.app.payloads.VendorDetailsDTO;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.VendorResponse;
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
    public APIResponse<VendorDetailsDTO> createVendor(VendorDetailsDTO storeDTO) {
        Vendor storeEntity = repoManager.getVendorRepo().save(modelMapper.map(storeDTO, Vendor.class));
        return APIResponse.success(HttpStatus.CREATED.value(), modelMapper.map(storeEntity, VendorDetailsDTO.class));
    }

    public VendorResponse<VendorDetailsDTO> fetchAllVendors(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Vendor> pageStores =  repoManager.getVendorRepo().findAll(pageDetails);
        List<Vendor> stores = pageStores.getContent();

        if (stores.isEmpty()) {
            throw new APIException(APIErrorCode.API_400,"No registered vendors found in database!!");
        }

        List<VendorDetailsDTO> storeDTOs = stores.stream().map(store -> modelMapper.map(store, VendorDetailsDTO.class))
                .collect(Collectors.toList());

        return new VendorResponse<>(
                storeDTOs,
                pageStores.getNumber(),
                pageStores.getSize(),
                pageStores.getTotalElements(),
                pageStores.getTotalPages(),
                pageStores.isLast()
        );
    }

    @Transactional
    public APIResponse<VendorDetailsDTO> updateStore(VendorDetailsDTO storeDTO, Long storeId) {
        Vendor savedStore =  repoManager.getVendorRepo().findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", storeId));
        modelMapper.map(storeDTO, savedStore);
        savedStore.setId(storeId);
        savedStore =  repoManager.getVendorRepo().save(savedStore);
        return APIResponse.success(HttpStatus.OK.value(), modelMapper.map(savedStore, VendorDetailsDTO.class));
    }

    @Transactional
    public APIResponse<String> deleteVendor(Long vendorId) {
        Vendor store =  repoManager.getVendorRepo().findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", vendorId));
        repoManager.getVendorRepo().deleteById(vendorId);
        return APIResponse.success(HttpStatus.OK.value(), "Store with id: " + vendorId + " deleted successfully !!!");

        // return storeRepo.delete(storeId);
    }

    public List<VendorDetailsDTO> fetchAllVendors() {
        List<Vendor> stores =  repoManager.getVendorRepo().findAll();
        if(stores.isEmpty()){
            return List.of();
        }
        return stores.stream().map(store -> modelMapper.map(store, VendorDetailsDTO.class))
                .collect(Collectors.toList());
    }


    @Cacheable(value = CacheType.CACHE_TYPE_VENDORS,key = "#status")
    public List<VendorBasicDTO> fetchVendorsByStatus(VendorStatus status) {
        var vendors=repoManager.getVendorRepo().findAllByStatus(status);
        return vendors.stream().map(vendor -> modelMapper.map(vendor, VendorBasicDTO.class)).toList();
    }


    @Cacheable(value = CacheType.CACHE_TYPE_VENDORS,key = "#vendorId")
    public VendorDetailsDTO fetchVendorById(Long vendorId) {
        var vendor =  repoManager.getVendorRepo().findById(vendorId)
                .orElseThrow(()-> new APIException(APIErrorCode.API_404,"Vendor not existed!!"));
        return modelMapper.map(vendor, VendorDetailsDTO.class);
    }

    //@Cacheable(value =CacheType.CACHE_TYPE_VENDORS,key = "#serviceArea")
    public List<VendorDetailsDTO> fetchVendorsByServiceArea(String serviceArea) {
       var vendorsList =   repoManager.getVendorRepo().findByServiceArea(serviceArea);
        return vendorsList.stream().map(vendor->modelMapper.map(vendor, VendorDetailsDTO.class)).toList();
    }

    @Cacheable(value =CacheType.CACHE_TYPE_VENDORS,key = "#vendorId")
    public Vendor fetchVendor(final Long vendorId){
        return repoManager.getVendorRepo().findById(vendorId).orElseThrow(() -> new APIException(APIErrorCode.API_400, "Vendor not exited in the system."));

    }

    public VendorResponse<VendorBasicDTO> fetchActiveVendorsByZipCode(String zipCode, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        log.debug("Fetch vendors for zipcode : {}",zipCode);
        return  processPaginationResult(repoManager.getVendorRepo().findActiveVendorsByZipCode(zipCode,pageDetails));

    }

    public VendorResponse<VendorBasicDTO> fetchActiveVendorsByZipCodeAndProduct(String zipCode,Long productId, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        log.debug("Fetch vendors for product : {}",productId);
        return  processPaginationResult(repoManager.getVendorRepo().findActiveVendorsByZipCode(zipCode,pageDetails));

    }

    @Cacheable(value =CacheType.CACHE_TYPE_VENDORS,key = "'ALL_VENDORS'")
    public Map<String, Object> fetchVendorsAndGroupedByCategory() {
        log.debug("Fetch all vendors ");
        return fetchVendorsByCategory(repoManager.getVendorRepo().findAllUniqueVendorsWithCategories());
    }


    private  VendorResponse<VendorBasicDTO> processPaginationResult(Page<Object[]> pageResult){
        List<Object[]> pageStores = pageResult.getContent();
        List<VendorBasicDTO> vendorsList = new ArrayList<>();

        for (Object[] result : pageStores) {
            Long vendorId = ((Number) result[0]).longValue();
            String businessName = (String) result[1];
            String bannerImage = (String) result[2];
            String serviceArea = (String) result[3];

            // Cast the category array to a list
            String[] categoriesArray = (String[]) result[4];
            List<String> categories = Arrays.asList(categoriesArray);

            // Create VendorCategoryDTO
            VendorBasicDTO vendor = new VendorBasicDTO(vendorId, businessName, bannerImage, serviceArea, categories);
            vendorsList.add(vendor);
        }
        return new VendorResponse<>(
                vendorsList,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );
    }

    private Map<String,Object> fetchVendorsByCategory(List<Object[]> rawResults){
        if(rawResults==null || rawResults.isEmpty()){
            log.info("No vendors found");
            return Collections.emptyMap();
        }
        List<VendorBasicDTO> vendorList = new ArrayList<>();
        Map<String, List<VendorBasicDTO>> categoryMap = new HashMap<>();

        for (Object[] result : rawResults) {
            Long vendorId = ((Number) result[0]).longValue();
            String businessName = (String) result[1];
            String bannerImage = (String) result[2];
            String serviceArea = (String) result[3];

            // Cast the category array to a list
            String[] categoriesArray = (String[]) result[4];
            List<String> categories = Arrays.asList(categoriesArray);

            // Create VendorCategoryDTO
            VendorBasicDTO vendor = new VendorBasicDTO(vendorId, businessName, bannerImage, serviceArea, categories);
            vendorList.add(vendor);

            // Add to map grouped by category
            for (String category : categories) {
                categoryMap
                        .computeIfAbsent(category, k -> new ArrayList<>())
                        .add(vendor);
            }
        }

        // Create response map with both formats
        Map<String, Object> response = new HashMap<>();
        response.put("all_vendors", vendorList);
        response.put("vendors_by_category", categoryMap);
        return response;
    }

}
