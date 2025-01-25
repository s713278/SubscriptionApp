package com.app.controllers;

import com.app.config.AppConstants;
import com.app.payloads.response.APIResponse;
import com.app.services.ServiceManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
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
@RequestMapping("/v1")
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
  @GetMapping("/vendors/service_area/categories")
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
  @GetMapping("/vendors/search")
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
  @GetMapping("/vendors/service_area/{service_area}")
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


  @Deprecated
  @Operation(
      summary = "Vendor's product/skus listing--DO NOT USE",
      description =
          """
          This API used for fetching all the assigned products and SKUs in order to
          display on vendor's skus listing page. <br>VendorsTest data: vendor_id:91
          """)
  @GetMapping("/vendors/{vendor_id}/all_product_skus")
  public ResponseEntity<APIResponse<?>> fetchVendorProductSkus(
      @PathVariable("vendor_id") @Schema(example = "91") Long vendorId) {
    var response = serviceManager.getSkuService().fetchAllProductSkusByVendorId(vendorId);
    return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
  }
   */

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
  @GetMapping("/vendors/{vendor_id}/products/skus")
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
  @GetMapping("/vendors/{vendor_id}/skus")
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

  /**
   * New Vendors (Id,Name,Thumbnail-->Horizontal scrolling) -->CTA --> Product Listing Page Top
   * Categories(Id,Name,Thumbnail -->Horizontal scrolling --> CTA--> List Vendors Product Offers -->
   * Land to Specific Vendor's Product Listing Page
   *
   * @param serviceArea
   * @return
   */
  @Operation(
      summary = "Home Page API",
      description =
          """
              This API is used for fetching new_vendors , offers based on user's location.
              <br> On click on the <b>new vendor image/name</b> ,User has to navigate to vendor's product listing page.
              <br> On click on <b>offer image/name</b> ,User has to navigate to vendor's specific sku's listing page.
              """,
      responses = {
        @ApiResponse(
            description = "Successful response",
            responseCode = "201",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = {
                      @ExampleObject(
                          name = "Example Success Response",
                          value =
                              """
                                  {
                                        "timestamp": "2025-01-25T06:30:42.7207679",
                                        "success": true,
                                        "status": 200,
                                        "data": {
                                          "new_vendors": [
                                            {
                                              "id": 91,
                                              "name": "H2A2 Farms",
                                              "image_path": "https://mithrabucket.s3.ap-south-1.amazonaws.com/images/vendors/91/H2A2.jpeg"
                                            },
                                            {
                                              "id": 54,
                                              "name": "Tastebuds Adventures Elite Catering Services",
                                              "image_path": "https://mithrabucket.s3.ap-south-1.amazonaws.com/images/category/South_indian_veg_thali.jpg"
                                            }
                                          ],
                                          "offers": [
                                            {
                                              "offerName": "10% Off On Vegetables",
                                              "imagePath": "https://mithrabucket.s3.ap-south-1.amazonaws.com/images/vegetables/tamato.jpeg",
                                              "vendor_id": 91,
                                              "product_id": 1
                                            },
                                            {
                                              "offerName": "20% Off On FarmStay and Visit",
                                              "imagePath": "https://mithrabucket.s3.ap-south-1.amazonaws.com/images/vegetables/beetroot.png",
                                              "vendor_id": 91,
                                              "product_id": 2
                                              }
                                            ]
                                          }
                                        }
                                  """)
                    }))
      })
  @GetMapping("/home")
  public ResponseEntity<APIResponse<?>> getHomePageData(
      @Schema(example = "Miyapur 502018") @RequestParam("service_area") String serviceArea) {
    List<VendorDTO> newVendors =
        Arrays.asList(
            new VendorDTO(
                91L,
                "H2A2 Farms",
                "https://mithrabucket.s3.ap-south-1.amazonaws.com/images/vendors/91/H2A2.jpeg"),
            new VendorDTO(
                54L,
                "Tastebuds Adventures Elite Catering Services",
                "https://mithrabucket.s3.ap-south-1.amazonaws.com/images/category/South_indian_veg_thali.jpg"));

    List<Offer> offers =
        Arrays.asList(
            new Offer(
                91L,
                1L,
                "10% Off On Vegetables",
                "https://mithrabucket.s3.ap-south-1.amazonaws.com/images/vegetables/tamato.jpeg"),
            new Offer(
                91L,
                2L,
                "20% Off On FarmStay and Visit",
                "https://mithrabucket.s3.ap-south-1.amazonaws.com/images/vegetables/beetroot.png"));
    return new ResponseEntity<>(
        APIResponse.success(new HomeResponse(newVendors, offers)), HttpStatus.OK);
  }

  @Data
  static class HomeResponse implements Serializable {
    @JsonProperty("new_vendors")
    private List<VendorDTO> newVendors;

    @JsonProperty("offers")
    private List<Offer> offers;

    public HomeResponse(List<VendorDTO> newVendors, List<Offer> paidPromotions) {
      this.newVendors = newVendors;
      this.offers = paidPromotions;
    }

    // Getters and setters omitted for brevity
  }

  @Data
  static class VendorDTO implements Serializable {
    private Long id;
    private String name;

    @JsonProperty("image_path")
    private String imagePath;

    public VendorDTO(Long id, String name, String imagePath) {
      this.id = id;
      this.name = name;
      this.imagePath = imagePath;
    }

    // Getters and setters omitted for brevity
  }

  @Data
  static class Offer implements Serializable {
    private String offerName;
    private String imagePath;

    @JsonProperty("vendor_id")
    private Long vendorId;

    @JsonProperty("product_id")
    private Long vendorProductId;

    public Offer(Long vendorId, Long vendorProductId, String offerName, String imagePath) {
      this.offerName = offerName;
      this.imagePath = imagePath;
      this.vendorId = vendorId;
      this.vendorProductId = vendorProductId;
    }
    // Getters and setters omitted for brevity
  }
}
