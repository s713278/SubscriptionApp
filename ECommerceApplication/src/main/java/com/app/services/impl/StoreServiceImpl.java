package com.app.services.impl;

import com.app.entites.Store;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.StoreDTO;
import com.app.payloads.response.StoreResponse;
import com.app.repositories.StoreRepo;
import com.app.services.StoreService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {

  private ModelMapper modelMapper;

  private StoreRepo storeRepo;

  @Override
  public StoreDTO createStore(StoreDTO storeDTO) {
    Store storeEntity = storeRepo.save(modelMapper.map(storeDTO, Store.class));
    return modelMapper.map(storeEntity, StoreDTO.class);
  }

  @Override
  public StoreResponse getStore(
      Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

    Sort sortByAndOrder =
        sortOrder.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
    Page<Store> pageStores = storeRepo.findAll(pageDetails);
    List<Store> stores = pageStores.getContent();

    if (stores.size() == 0) {
      throw new APIException("No stores is created till now");
    }

    List<StoreDTO> storeDTOs =
        stores.stream()
            .map(store -> modelMapper.map(store, StoreDTO.class))
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
  public StoreDTO updateStore(StoreDTO storeDTO, Long storeId) {
    Store savedStore =
        storeRepo
            .findById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", storeId));
    modelMapper.map(storeDTO, savedStore);
    savedStore.setId(storeId);
    savedStore = storeRepo.save(savedStore);
    return modelMapper.map(savedStore, StoreDTO.class);
  }

  @Override
  public String deleteStore(Long storeId) {
    Store store =
        storeRepo
            .findById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", storeId));
    storeRepo.deleteById(storeId);
    return "Store with id: " + storeId + " deleted successfully !!!";

    // return storeRepo.delete(storeId);
  }
}
