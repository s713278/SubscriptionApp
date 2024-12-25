package com.app.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.config.AppConstants;
import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.VendorStatus;
import com.app.payloads.request.VendorProfileRequest;
import com.app.payloads.response.APIResponse;
import com.app.services.ServiceManager;
import com.app.services.auth.dto.UserAuthentication;
import com.app.services.validator.AddressValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "6. Vendor Profile Management")
@RestController
@RequestMapping("/v1/vendors")

@SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
@Slf4j
@RequiredArgsConstructor
public class VendorController {

    private final ServiceManager serviceManager;
    private final AddressValidator addressValidator;


    @PostMapping("/")
    @PreAuthorize("(hasAuthority('VENDOR') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    @Operation(summary = "Create Vendor Profile",description = "Profile can be created by  Vendor/Admin/Customer_Care after login.")
    public ResponseEntity<APIResponse<?>> createVendorProfile(@Valid @RequestBody VendorProfileRequest vendorDetails, Authentication authentication) {
        var response =serviceManager.getVendorService().createVendorProfile(vendorDetails,authentication);
        return new ResponseEntity<>(APIResponse.success(HttpStatus.CREATED.value(),response), HttpStatus.CREATED);
    }

    @PreAuthorize("#vendorId == authentication.principal and (hasAuthority('ADMIN'))")
    @Operation(summary = "Fetch all vendors",description = "Fetch all vendors by admin role only")
    @GetMapping("/")
    public ResponseEntity<APIResponse<?>> fetchAllActiveVendors() {
        log.debug("Request received for all vendors");
        return new ResponseEntity<>(APIResponse.success(serviceManager.getVendorService().fetchVendorsAndGroupedByCategory()),HttpStatus.OK);
    }

    @PreAuthorize("#vendorId == authentication.principal OR (hasAuthority('VENDOR') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    @PutMapping("/{vendorId}")
    @Operation(summary = "Update vendor full profile",description = "Update vendor full profile by Vendor/Admin/Customer_Care after login")
    public ResponseEntity<APIResponse<?>> updateVendor(@RequestBody VendorProfileRequest storeDTO,
                                                                     @PathVariable Long vendorId,Authentication authentication) {
        var response = serviceManager.getVendorService().updateVendorProfile(storeDTO, vendorId,authentication);
        return new ResponseEntity<>(APIResponse.success(HttpStatus.OK.value(),response), HttpStatus.OK);
    }

    @Operation(summary = "Access vendor profile by vendorId",description = "Fetch vendor profile by Vendor/Admin/Customer_Care after login.")
    @PreAuthorize("#vendorId == authentication.principal OR (hasAuthority('VENDOR') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    @GetMapping("/{vendorId}")
    public ResponseEntity<APIResponse<?>> getVendorProfileById(@PathVariable Long vendorId, Authentication authentication) {
        UserAuthentication userAuthentication= (UserAuthentication)authentication;
        var response = serviceManager.getVendorService().fetchVendorById(vendorId, userAuthentication);
        return new ResponseEntity<>(APIResponse.success(HttpStatus.OK.value(),response), HttpStatus.OK);
    }

    @Operation(summary = "Search vendor profile by mobile",description = "Fetch vendor profile by Admin/Customer_Care after login.")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    @GetMapping("/{mobile}")
    public ResponseEntity<APIResponse<?>> getVendorProfileByMobile(@PathVariable String mobile, Authentication authentication) {
        log.debug("Request received for fetching vendor profile for mobile #{}",mobile);
        UserAuthentication userAuthentication= (UserAuthentication)authentication;
        var response = serviceManager.getVendorService().fetchVendorByMobile(mobile, userAuthentication);
        return new ResponseEntity<>(APIResponse.success(HttpStatus.OK.value(),response), HttpStatus.OK);
    }

   /* @PatchMapping("/{vendorId}")
    @PreAuthorize("#vendorId == authentication.principal OR (hasAuthority('VENDOR') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    public ResponseEntity<APIResponse<?>> updateBusinessAddress(@PathVariable Long vendorId,
                                                            @RequestBody Map<String,String> address) {
        addressValidator.validateAddress(vendorId, address);
        return ResponseEntity.ok(APIResponse.success("Business address updated successfully."));
    }*/

    @Operation(summary = "Review Vendor", description = "Update vendor's approval status (Accepted/Rejected by admin after login.")
    @PatchMapping("/{vendorId}/approval_status")
    @PreAuthorize("(hasAuthority('ADMIN')")
    public ResponseEntity<APIResponse<?>> updateApprovalStatus(@PathVariable Long vendorId,
                                                           @RequestBody ApprovalStatus approvalStatus) {
        serviceManager.getVendorService().updateApprovalStatus(vendorId,approvalStatus);
        return ResponseEntity.ok(APIResponse.success("Vendor approval's status changed successfully."));
    }

    @Operation(summary = "Update vendor status", description = "Update vendor status(Active/InActive by Admin/Customer_Care after login.")
    @PatchMapping("/{vendorId}/vendor_status")
    @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    public ResponseEntity<APIResponse<?>> updateVendorStatus(@PathVariable Long vendorId,
                                                               @RequestBody VendorStatus vendorStatus) {
        serviceManager.getVendorService().updateVendorStatus(vendorId,vendorStatus);
        return ResponseEntity.ok(APIResponse.success("Vendor status changed successfully."));
    }

    @Operation(summary = "Assign categories", description = "Assign Categories to vendor by Admin/Customer_Care")
    @PatchMapping("/{vendorId}/categories")
    @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    public ResponseEntity<APIResponse<?>> assignCategories(@PathVariable Long userId,
                                                                @RequestBody Long[] categoryIds) {
        return ResponseEntity.ok(APIResponse.success("Categories assigned successfully."));
    }

    @Operation(summary = "Assign products", description = "Assign Categories to vendor by Admin/Customer_Care")
    @PatchMapping("/{vendorId}/products")
    @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    public ResponseEntity<APIResponse<?>> assignProducts(@PathVariable Long vendorId,
                                                         @RequestBody Map<Long,Long[]> catProductsMap) {
        return ResponseEntity.ok(APIResponse.success("Products assigned successfully."));
    }
}
