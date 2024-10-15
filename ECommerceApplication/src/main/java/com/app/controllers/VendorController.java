package com.app.controllers;

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

import com.app.config.AppConstants;
import com.app.payloads.VendorDTO;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.StoreResponse;
import com.app.services.impl.VendorService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "5. Vendor Management")
@RestController
@RequestMapping("/stores")
@SecurityRequirement(name = "E-Commerce Application")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService storeService;

    @PostMapping("/")
    public ResponseEntity<APIResponse<VendorDTO>> createStore(@Valid @RequestBody VendorDTO storeDTO) {
        return new ResponseEntity<>(storeService.createStore(storeDTO), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse<?>> getAllStores() {
        return new ResponseEntity<>(APIResponse.success(storeService.getAllVendors()),HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<StoreResponse> getStores(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_STORE_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        StoreResponse storeResponse = storeService.getStore(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<StoreResponse>(storeResponse, HttpStatus.FOUND);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<APIResponse<VendorDTO>> updateStore(@RequestBody VendorDTO storeDTO,
            @PathVariable Long storeId) {
        return new ResponseEntity<>(storeService.updateStore(storeDTO, storeId), HttpStatus.OK);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<APIResponse<String>> deleteStore(@PathVariable Long storeId) {
        return new ResponseEntity<>(storeService.deleteStore(storeId), HttpStatus.OK);
    }
}
