package com.app.controllers;

import com.app.config.AppConstants;
import com.app.payloads.StoreDTO;
import com.app.payloads.response.StoreResponse;
import com.app.services.StoreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class StoreController {

  private StoreService storeService;

  @PostMapping("/public/store")
  public ResponseEntity<StoreDTO> createStore(@Valid @RequestBody StoreDTO storeDTO) {
    StoreDTO savedStoreDTO = storeService.createStore(storeDTO);
    return new ResponseEntity<StoreDTO>(savedStoreDTO, HttpStatus.CREATED);
  }

  @GetMapping("/admin/public/stores")
  public ResponseEntity<StoreResponse> getCategories(
      @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)
          Integer pageNumber,
      @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)
          Integer pageSize,
      @RequestParam(
              name = "sortBy",
              defaultValue = AppConstants.SORT_CATEGORIES_BY,
              required = false)
          String sortBy,
      @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false)
          String sortOrder) {

    StoreResponse categoryResponse = storeService.getStore(pageNumber, pageSize, sortBy, sortOrder);

    return new ResponseEntity<StoreResponse>(categoryResponse, HttpStatus.FOUND);
  }

  @PutMapping("/public/stores/{storeId}")
  public ResponseEntity<StoreDTO> updateStore(
      @RequestBody StoreDTO storeDTO, @PathVariable Long storeId) {
    StoreDTO updatedStoreDTO = storeService.updateStore(storeDTO, storeId);
    return new ResponseEntity<StoreDTO>(updatedStoreDTO, HttpStatus.OK);
  }

  @DeleteMapping("/admin/stores/{storeId}")
  public ResponseEntity<String> deleteStore(@PathVariable Long storeId) {
    String status = storeService.deleteStore(storeId);
    return new ResponseEntity<String>(status, HttpStatus.OK);
  }
}
