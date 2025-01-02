package com.app.controllers;

import com.app.config.AppConstants;
import com.app.payloads.response.APIResponse;
import com.app.services.ServiceManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "2. Vendor's Listing API", description = "APIs for listing vendors based on zipcode.")
@RestController
@RequestMapping("/v1/vendors")
@Slf4j
@RequiredArgsConstructor
public class VendorListingController {

  private final ServiceManager serviceManager;

  @Operation(
      summary = "Vendor's listing by zipcode and/or category",
      description =
          "This API used for fetching categories based on <b>zipcode</b>.<br>"
              + "This API also used for fetching the categories by <b>zipcode</b> and <b>category_id</b><br>"
              + "<b>Test data: zipcode</b>: 502108,502103")
  @GetMapping("zipcode/{zipcode}")
  public ResponseEntity<APIResponse<?>> fetchActiveVendorsByZipCode(
      @PathVariable("zipcode") String zipCode,
      @RequestParam(name = "category_id", required = false) Long categoryId,
      @RequestParam(
              name = AppConstants.REQ_PARAM_PAGE_NUMBER,
              defaultValue = AppConstants.PAGE_NUMBER,
              required = false)
          Integer pageNumber,
      @RequestParam(
              name = AppConstants.REQ_PARAM_PAGE_SIZE,
              defaultValue = AppConstants.PAGE_SIZE,
              required = false)
          Integer pageSize) {
    log.debug(
        "Request received for fetching vendors for zipcode :{} and/or categoryId: {}",
        zipCode,
        categoryId);
    return new ResponseEntity<>(
        APIResponse.success(
            serviceManager
                .getVendorService()
                .fetchActiveVendorsByZipCode(zipCode, categoryId, pageNumber, pageSize)),
        HttpStatus.OK);
  }

  /*
   @Operation(summary = "Vendors search by zipcode and product_id",description = "This API used for searching ")
   @GetMapping("/{zipcode}/{product_id}")
   public ResponseEntity<APIResponse<?>> fetchActiveVendorsByProduct(
       @PathVariable("zipcode") String zipCode,
       @PathVariable("product_id") Long productId,
       @RequestParam(name = "page_number", defaultValue = AppConstants.PAGE_NUMBER, required = false)
           Integer pageNumber,
       @RequestParam(name = "page_size", defaultValue = AppConstants.PAGE_SIZE, required = false)
           Integer pageSize) {
     log.debug("Request received for fetching vendors for productId : {}", productId);
     return new ResponseEntity<>(
         APIResponse.success(
             serviceManager
                 .getVendorService()
                 .fetchActiveVendorsByZipCodeAndProduct(zipCode, productId, pageNumber, pageSize)),
         HttpStatus.OK);
   }
  */

  @Operation(summary = "Vendor's products listing", description = "Test data: vendor_id:91")
  @GetMapping("/{vendor_id}/products")
  public ResponseEntity<APIResponse<?>> fetchVendorProductSkus(
      @PathVariable("vendor_id") Long vendorId) {
    var response = serviceManager.getSkuService().fetchProductSkusByVendorId(vendorId);
    return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
  }
}
