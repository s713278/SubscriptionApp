package com.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.config.AppConstants;
import com.app.entites.type.VendorStatus;
import com.app.payloads.response.APIResponse;
import com.app.services.ServiceManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "2. Vendor's Listing")
@RestController
@RequestMapping("/v1/vendors")

@Slf4j
@RequiredArgsConstructor
public class VendorListingController {

    private final ServiceManager serviceManager;

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN'))")
    @Operation(summary = "All vendors listing")
    @GetMapping("/")
    public ResponseEntity<APIResponse<?>> fetchAllActiveVendors() {
        log.debug("Request received for all vendors");
        return new ResponseEntity<>(APIResponse.success(serviceManager.getVendorService().fetchVendorsAndGroupedByCategory()),HttpStatus.OK);
    }

    @Operation(summary = "Vendor's listing by zipcode")
    @GetMapping("/{zipCode}")
    public ResponseEntity<APIResponse<?>> fetchActiveVendorsByZipCode(@PathVariable String zipCode,
                                                                         @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                         @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
                                                                        ) {
        log.debug("Request received for fetching vendors for zipcode : {}",zipCode);
        return new ResponseEntity<>(APIResponse.success(serviceManager.getVendorService().fetchActiveVendorsByZipCode(zipCode,pageNumber,pageSize)),HttpStatus.OK);
    }

    @Operation(summary = "Vendor's listing by zipcode and category")
    @GetMapping("/{zipCode}/{categoryId}")
    public ResponseEntity<APIResponse<?>> fetchActiveVendorsByCategory(@PathVariable String zipCode,
                                                                      @PathVariable Long categoryId,
                                                                      @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                      @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        log.debug("Request received for fetching vendors for categoryId : {}",categoryId);
        return new ResponseEntity<>(APIResponse.success(serviceManager.getVendorService()
                .fetchActiveVendorsByZipCodeAndProduct(zipCode,categoryId,pageNumber,pageSize)),HttpStatus.OK);
    }

    @Operation(summary = "Vendors search by zipcode and product name")
    @GetMapping("/{zipCode}/{productName}")
    public ResponseEntity<APIResponse<?>> fetchActiveVendorsByProduct(@PathVariable String zipCode,
                                                                      @PathVariable Long productName,
                                                                      @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                      @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        log.debug("Request received for fetching vendors for productId : {}",productName);
        return new ResponseEntity<>(APIResponse.success(serviceManager.getVendorService()
                .fetchActiveVendorsByZipCodeAndProduct(zipCode,productName,pageNumber,pageSize)),HttpStatus.OK);
    }

    @Operation(summary = "Vendor's products listing")
    @GetMapping("/{vendorId}/products")
    public ResponseEntity<APIResponse<?>> fetchVendorProductSkus(@PathVariable  Long vendorId) {
        var response =serviceManager.getSkuService().fetchProductSkusByVendorId(vendorId);
        return new ResponseEntity<>(APIResponse.success(response),HttpStatus.OK);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN'))")
    @Operation(summary = "Fetch vendors with a given status")
    @GetMapping("/status/{status}")
    public ResponseEntity<APIResponse<?>> fetchVendorsByStatus(@PathVariable VendorStatus status) {
        return new ResponseEntity<>(APIResponse.success(serviceManager.getVendorService().fetchVendorsByStatus(status)),HttpStatus.OK);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN'))")
    @Operation(summary = "All active vendors with pagination")
    @GetMapping("/all")
    public ResponseEntity<APIResponse<?>> getAllVendors(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_STORE_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        var vendorResponse = serviceManager.getVendorService().fetchAllVendors(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(APIResponse.success(vendorResponse),HttpStatus.OK);
    }

    @Operation(summary = "Fetch Subscriptions By Vendor ID")
    @GetMapping("/{vendorId}/subs")
    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('VENDOR'))")
    public ResponseEntity<APIResponse<?>> fetchSubsByVendorId( @PathVariable Long vendorId) {
       var subscriptions = serviceManager.getSubscriptionService().fetchSubsByVendor(vendorId);
        return ResponseEntity.ok(APIResponse.success(subscriptions));
    }
}
