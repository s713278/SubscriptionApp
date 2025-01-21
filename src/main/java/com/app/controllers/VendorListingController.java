package com.app.controllers;

import com.app.config.AppConstants;
import com.app.payloads.response.APIResponse;
import com.app.services.ServiceManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "2. Vendors and Products Listing API",
    description =
        "APIs for fetching vendors listing and SKU listing in order to display on UI for User journey.")
@RestController
@RequestMapping("/v1/vendors")
@Slf4j
@RequiredArgsConstructor
public class VendorListingController {

  private final ServiceManager serviceManager;

  @Operation(
      summary = "Fetch Categories by service_area",
      description =
          """
              This API used for fetching all categories served by the vendors based on service_area/zipcode.
              <br>Test data: service_area:502108,502103,Siddpet,Mayurinagar.
          """)
  @GetMapping("service_area/categories")
  public ResponseEntity<APIResponse<?>> fetchCategoriesByZipCode(
      @RequestParam(value = "service_area", required = true) @Schema(example = "502108")
          String serviceArea) {
    log.debug("Request received for fetching categories for service area :{} ", serviceArea);
    var response = serviceManager.getCategoryService().fetchCategoriesByServiceArea(serviceArea);
    return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
  }

  // TODO: Actual implementation is pending
  @Operation(
      summary = "Searching vendors by keyword[ category name,product name and sku name ]",
      description =
          """
                      This API used for search and list marched vendors for given pattern.
                      """)
  @GetMapping("/search")
  public ResponseEntity<APIResponse<?>> searchVendorsByKeyword(
      @RequestParam(value = "service_area", required = false) @Schema(example = "502108")
          String serviceArea,
      @RequestParam(value = "keyword", required = true) @Schema(example = "Catering")
          String keyword,
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
    log.debug("Request received for searching vendors by keyword :{}", keyword);
    return new ResponseEntity<>(
        APIResponse.success(
            serviceManager
                .getVendorService()
                .fetchActiveVendorsByServiceArea("502108", null, pageNumber, pageSize)),
        HttpStatus.OK);
  }

  @Operation(
      summary = "Fetch Vendors by service_area/zipcode with/without category_id",
      description =
          """
              This API used for fetching all the vendors and specific vendors based on zipcode and/or category_id.
              <br>If <b>category_id</b> is NULL: Vendors will be filtered by zipcode only.
              <br>If <b>category_id</b> is not NULL: Vendors will be filtered by zipcode & product_id.
              <br>Test data: zipcode:502108,502103,Siddipet,Hyderabad and category_id: 102,103
              """)
  @GetMapping("service_area/{service_area}")
  public ResponseEntity<APIResponse<?>> fetchActiveVendorsByServiceArea(
      @PathVariable("service_area") @Schema(example = "502108") String serviceArea,
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
        "Request received for fetching vendors for service_area :{} and/or categoryId: {}",
        serviceArea,
        categoryId);
    return new ResponseEntity<>(
        APIResponse.success(
            serviceManager
                .getVendorService()
                .fetchActiveVendorsByServiceArea(serviceArea, categoryId, pageNumber, pageSize)),
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

  @Deprecated
  @Operation(
      summary = "Vendor's product/skus listing--DO NOT USE",
      description =
          """
          This API used for fetching all the assigned products and SKUs in order to
          display on vendor's skus listing page. <br>VendorsTest data: vendor_id:91
          """)
  @GetMapping("/{vendor_id}/all_product_skus")
  public ResponseEntity<APIResponse<?>> fetchVendorProductSkus(
      @PathVariable("vendor_id") @Schema(example = "91") Long vendorId) {
    var response = serviceManager.getSkuService().fetchAllProductSkusByVendorId(vendorId);
    return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
  }

  @Operation(
      summary = "Fetch vendor assigned products",
      description =
          "This API fetches assigned products(array of {id,name}) in order to display the menu option vendor's product/skus listing page.")
  @GetMapping("/{vendor_id}/products")
  public ResponseEntity<APIResponse<?>> fetchAssignCategories(
      @Schema(example = "91") @PathVariable("vendor_id") Long vendorId) {
    var response = serviceManager.getVendorService().fetchAssignedProducts(vendorId);
    return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
  }

  @Operation(
      summary = "Fetch SKUs with vendor_id and/or without product_id",
      description =
          """
  This API used for fetching all the SKUs and specific SKUs based on vendor_id and/or product_id.
  <br>If <b>product_id</b> is NULL: SKUs will be filtered by vendor_id only.
  <br>If <b>product_id</b> is not NULL: SKUs will be filtered by vendor_id & product_id.
  <br>Test data: vendor_id:91 and Items product_id:1 and Service product_id: 2
  """)
  @GetMapping("/{vendor_id}/products/skus")
  public ResponseEntity<APIResponse<?>> fetchSkusByVendorProduct(
      @Schema(example = "91") @PathVariable("vendor_id") Long vendorId,
      @RequestParam(value = "product_id", required = false) Long productId,
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
    log.info("Request received for fetching skus by vendor product id:{}", productId);
    var response =
        serviceManager
            .getVendorService()
            .fetchSkusByVendorProductId(vendorId, productId, pageNumber, pageSize);
    return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
  }

  @Operation(
      summary = "SKU Search by category name,product name and sku name in vendor context",
      description =
          """
                      This API used for search and list matched skus for given search criteria.
                      """)
  @GetMapping("/{vendor_id}/skus")
  public ResponseEntity<APIResponse<?>> searchSkusByKeyword(
      @Schema(example = "91") @PathVariable("vendor_id") Long vendorId,
      @Schema(example = "milk") @RequestParam(value = "keyword", required = true) String keyword,
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
    log.info("Request received for search skus by keyword?:{}", keyword);
    var response =
        serviceManager
            .getVendorService()
            .fetchSkusByVendorProductId(vendorId, null, pageNumber, pageSize);
    return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
  }
}
