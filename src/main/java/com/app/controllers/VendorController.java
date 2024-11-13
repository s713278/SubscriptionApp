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
import com.app.services.ServiceManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "5. Vendor Management")
@RestController
@RequestMapping("/v1/vendors")

@RequiredArgsConstructor
public class VendorController {

    private final ServiceManager serviceManager;

    @SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
    @PostMapping("/")
    public ResponseEntity<APIResponse<VendorDTO>> createStore(@Valid @RequestBody VendorDTO storeDTO) {
        return new ResponseEntity<>(serviceManager.getVendorService().createVendor(storeDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Fetch vendors with a given status")
    @GetMapping("/{status}")
    public ResponseEntity<APIResponse<?>> fetchVendorsByStatus(@PathVariable  String status) {
        return new ResponseEntity<>(APIResponse.success(serviceManager.getVendorService().fetchVendorsByStatus(status)),HttpStatus.OK);
    }

    @Operation(summary = "Vendor's products listing")
    @GetMapping("/{vendorId}/products")
    public ResponseEntity<APIResponse<?>> fetchVendorProductSkus(@PathVariable  Long vendorId) {
        var response =serviceManager.getSkuService().fetchProductSkusByVendorId(vendorId);
        return new ResponseEntity<>(APIResponse.success(response),HttpStatus.OK);
    }

    @Operation(summary = "All vendors without pagination")
    @GetMapping("/list")
    public ResponseEntity<APIResponse<?>> fetchAllVendors() {
        return new ResponseEntity<>(APIResponse.success(serviceManager.getVendorService().fetchAllVendors()),HttpStatus.OK);
    }

    @Operation(summary = "All vendors with pagination")
    @GetMapping("/")
    public ResponseEntity<APIResponse<?>> getAllVendors(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_STORE_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        StoreResponse storeResponse = serviceManager.getVendorService().fetchAllVendors(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(APIResponse.success(storeResponse),HttpStatus.OK);
    }

    @SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
    @PutMapping("/{vendorId}")
    public ResponseEntity<APIResponse<VendorDTO>> updateStore(@RequestBody VendorDTO storeDTO,
            @PathVariable Long vendorId) {
        return new ResponseEntity<>(serviceManager.getVendorService().updateStore(storeDTO, vendorId), HttpStatus.OK);
    }

    @SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
    @DeleteMapping("/{vendorId}")
    public ResponseEntity<APIResponse<String>> deleteStore(@PathVariable Long vendorId) {
        return new ResponseEntity<>(serviceManager.getVendorService().deleteVendor(vendorId), HttpStatus.OK);
    }
}
