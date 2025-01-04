package com.app.controllers;

import com.app.config.AppConstants;
import com.app.controllers.validator.AbstractRequestValidation;
import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.VendorStatus;
import com.app.payloads.LegalDetailsDTO;
import com.app.payloads.request.AssignCategoriesRequest;
import com.app.payloads.request.AssignProductsRequest;
import com.app.payloads.request.SkuCreateRequest;
import com.app.payloads.request.VendorProfileRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.VendorProfileResponse;
import com.app.services.ServiceManager;
import com.app.services.auth.dto.UserAuthentication;
import com.app.services.validator.AddressValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "6. Vendor Profile API",
    description =
        """
            APIS for creating,managing vendor profiles,catalogue. These API can be accessed by VENDOR or ADMIN role only.
            """)
@RestController
@RequestMapping("/v1/vendors")
@SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
@Slf4j
@RequiredArgsConstructor
public class VendorController extends AbstractRequestValidation {

  private final ServiceManager serviceManager;
  private final AddressValidator addressValidator;

  @PostMapping("/")
  @PreAuthorize(
      "(hasAuthority('VENDOR') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
  @Operation(
      summary = "Create Vendor Profile",
      description =
          """
          Profile can be created by Vendor/Admin/Customer_Care after login.
            <br>
              <b>Test data</b>
            </br>
            <br>Admin : 9948023105 </br>
            <br>Vendor : 9948023190 </br>
            OTP is default one to verify the APIs.""")
  public ResponseEntity<APIResponse<?>> createVendorProfile(
      @Valid @RequestBody VendorProfileRequest vendorDetails,
      BindingResult bindingResult,
      Authentication authentication) {
    log.info(
        "Received request for creating vendor profile by logged in user {}",
        authentication.getPrincipal());
    validateRequest(bindingResult);
    addressValidator.validateAddress(vendorDetails.getBusinessAddress());
    addressValidator.validateServiceAreas(vendorDetails.getServiceAreas());
    var response =
        serviceManager.getVendorService().createVendorProfile(vendorDetails, authentication);
    return new ResponseEntity<>(
        APIResponse.success(HttpStatus.CREATED.value(), response), HttpStatus.CREATED);
  }

  @PreAuthorize("(hasAuthority('ADMIN'))")
  @Operation(summary = "Fetch all vendors", description = "Fetch all vendors by admin role only")
  @GetMapping("/")
  public ResponseEntity<APIResponse<?>> getAllVendors(
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
    var response =
        serviceManager.getVendorService().fetchAllVendors(pageNumber, pageSize, sortBy, sortOrder);
    return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
  }

  @PreAuthorize(
      "#vendorId == authentication.principal OR (hasAuthority('VENDOR') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
  @PutMapping("/{vendor_id}")
  @Operation(
      summary = "Update vendor full profile",
      description = "Update vendor full profile by Vendor/Admin/Customer_Care after login")
  public ResponseEntity<APIResponse<?>> updateVendor(
      @RequestBody VendorProfileRequest storeDTO,
      @PathVariable("vendor_id") Long vendorId,
      Authentication authentication) {
    var response =
        serviceManager.getVendorService().updateVendorProfile(storeDTO, vendorId, authentication);
    return new ResponseEntity<>(
        APIResponse.success(HttpStatus.OK.value(), response), HttpStatus.OK);
  }

  @Operation(
      summary = "Access vendor profile by vendorId",
      description = "Fetch vendor profile by Vendor/Admin/Customer_Care after login.")
  @PreAuthorize(
      "((#vendorId == authentication.principal AND hasAuthority('VENDOR'))  or (hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE')))")
  @GetMapping("/{vendor_id}")
  public ResponseEntity<APIResponse<?>> getVendorProfileById(
      @PathVariable("vendor_id") Long vendorId, Authentication authentication) {
    UserAuthentication userAuthentication = (UserAuthentication) authentication;
    var response = serviceManager.getVendorService().fetchVendorById(vendorId, userAuthentication);
    return new ResponseEntity<>(
        APIResponse.success(HttpStatus.OK.value(), response), HttpStatus.OK);
  }

  @Operation(
      summary = "Search vendor profile by vendor id or mobile",
      description = "Fetch vendor profile by Admin/Customer_Care after login.")
  @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
  @GetMapping("/search/{id}")
  public ResponseEntity<APIResponse<?>> getVendorProfileByMobile(
      @PathVariable @Valid String id, Authentication authentication) {
    log.debug("Request received for fetching vendor profile for vendor id / mobile #{}", id);
    UserAuthentication userAuthentication = (UserAuthentication) authentication;
    VendorProfileResponse response = null;
    if (id.trim().length() == 10) {
      response =
          serviceManager.getVendorService().fetchVendorByMobile(id.trim(), userAuthentication);
    } else {
      response =
          serviceManager
              .getVendorService()
              .fetchVendorById(Long.parseLong(id.trim()), userAuthentication);
    }
    return new ResponseEntity<>(
        APIResponse.success(HttpStatus.OK.value(), response), HttpStatus.OK);
  }

  @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
  @PatchMapping("/{vendor_id}/legal_details")
  @Operation(
      summary = "Add legal details",
      description = "Add legal details by Admin/Customer_Care after login")
  public ResponseEntity<APIResponse<?>> addLegalDetails(
      @PathVariable("vendor_id") Long vendorId,
      @RequestBody LegalDetailsDTO legalDetails,
      Authentication authentication) {
    serviceManager.getVendorService().addVendorLegalDetails(vendorId, legalDetails, authentication);
    return new ResponseEntity<>(
        APIResponse.success(HttpStatus.OK.value(), "Legal details added successfully."),
        HttpStatus.OK);
  }

  /* @PatchMapping("/{vendorId}")
  @PreAuthorize("#vendorId == authentication.principal OR (hasAuthority('VENDOR') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
  public ResponseEntity<APIResponse<?>> updateBusinessAddress(@PathVariable Long vendorId,
                                                          @RequestBody Map<String,String> address) {
      addressValidator.validateAddress(vendorId, address);
      return ResponseEntity.ok(APIResponse.success("Business address updated successfully."));
  }*/

  @Operation(
      summary = "Approve Vendor",
      description = "Update vendor's approval status (Accepted/Rejected by admin after login.")
  @PatchMapping("/{vendor_id}/approval_status")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<APIResponse<?>> updateApprovalStatus(
      @PathVariable("vendor_id") Long vendorId, @RequestBody ApprovalStatus approvalStatus) {
    serviceManager.getVendorService().updateApprovalStatus(vendorId, approvalStatus);
    return ResponseEntity.ok(APIResponse.success("Vendor approval's status changed successfully."));
  }

  @Operation(
      summary = "Update vendor status",
      description = "Update vendor status(Active/InActive by Admin/Customer_Care after login.")
  @PatchMapping("/{vendor_id}/vendor_status")
  @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
  public ResponseEntity<APIResponse<?>> updateVendorStatus(
      @PathVariable("vendor_id") Long vendorId, @RequestBody VendorStatus vendorStatus) {
    serviceManager.getVendorService().updateVendorStatus(vendorId, vendorStatus);
    return ResponseEntity.ok(APIResponse.success("Vendor status changed successfully."));
  }

  @Operation(
      summary = "Assign categories to a vendor",
      description = "Assign one or more categories to a vendor by Admin/Customer_Care role")
  @PatchMapping("/{vendor_id}/categories")
  @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
  public ResponseEntity<APIResponse<?>> assignCategories(
      @PathVariable("vendor_id") Long vendorId,
      @RequestBody AssignCategoriesRequest assignCategoriesRequest) {
    serviceManager.getVendorService().assignCategories(vendorId, assignCategoriesRequest);
    return ResponseEntity.ok(APIResponse.success("Categories assigned successfully."));
  }

  @Operation(
      summary = "Assign products to a vendor",
      description =
          "Assigns one or more products to a vendor with their features by Admin/Customer_Care role",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              content =
                  @Content(
                      mediaType = "application/json",
                      examples =
                          @ExampleObject(
                              value =
                                  """
                                        {
                                          "102": [
                                            {
                                              "product_id": 1002,
                                              "features_map": {
                                                "color": "red"
                                              }
                                            }
                                          ]
                                        }
                                        """))))
  @PatchMapping("/{vendor_id}/products")
  @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
  public ResponseEntity<APIResponse<?>> assignProducts(
      @PathVariable("vendor_id") @Schema(example = "91") Long vendorId,
      @RequestBody Map<Long, List<AssignProductsRequest>> assignProductsRequest) {
    serviceManager.getVendorService().assignProducts(vendorId, assignProductsRequest);
    return ResponseEntity.ok(APIResponse.success("Products assigned successfully."));
  }

  @PreAuthorize(
      "(hasAuthority('VENDOR') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
  @PostMapping("/{vendor_id}/{vendor_product_id}")
  @Operation(
      summary = "Create Vendor Sku",
      description = "Create Vendor Sku by Vendor/Admin/Customer_Care after login")
  public ResponseEntity<APIResponse<?>> createVendorSku(
      @PathVariable("vendor_id") @Schema(example = "91") Long vendorId,
      @PathVariable("vendor_product_id") @Schema(example = "10002") Long vendorProductId,
      @RequestBody @Valid SkuCreateRequest request,
      BindingResult bindingResult,
      Authentication authentication) {
    log.info("Request received for creating a new SKU by {}", authentication.getPrincipal());
    validateRequest(bindingResult);
    var response = serviceManager.getSkuService().createSku(vendorId, vendorProductId, request);
    return new ResponseEntity<>(
        APIResponse.success(HttpStatus.OK.value(), response), HttpStatus.CREATED);
  }
}
