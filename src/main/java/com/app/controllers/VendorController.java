package com.app.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.config.AppConstants;
import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.VendorStatus;
import com.app.payloads.VendorDetailsDTO;
import com.app.payloads.response.APIResponse;
import com.app.services.ServiceManager;
import com.app.services.validator.AddressValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "6. Vendor Management")
@RestController
@RequestMapping("/v1/vendors")

@Slf4j
@RequiredArgsConstructor
public class VendorController {

    private final ServiceManager serviceManager;
    private final AddressValidator addressValidator;

    @SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
    @PostMapping("/")
    @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    public ResponseEntity<APIResponse<?>> createVendorProfile(@Valid @RequestBody VendorDetailsDTO vendorDetails) {
        var response =serviceManager.getVendorService().createVendor(vendorDetails);
        return new ResponseEntity<>(APIResponse.success(HttpStatus.CREATED.value(),response), HttpStatus.CREATED);
    }

    @PreAuthorize("#vendorId == authentication.principal OR (hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    @SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
    @PutMapping("/{vendorId}")
    public ResponseEntity<APIResponse<?>> updateVendor(@RequestBody VendorDetailsDTO storeDTO,
                                                                     @PathVariable Long vendorId) {
        var response = serviceManager.getVendorService().updateVendor(storeDTO, vendorId);
        return new ResponseEntity<>(APIResponse.success(HttpStatus.OK.value(),response), HttpStatus.OK);
    }

    @PatchMapping("/{vendorId}")
    @PreAuthorize("#vendorId == authentication.principal OR (hasAuthority('ADMIN') or hasAuthority('USER'))")
    public ResponseEntity<APIResponse<?>> updateBusinessAddress(@PathVariable Long vendorId,
                                                            @RequestBody Map<String,String> address) {
        addressValidator.validateAddress(vendorId, address);
        return ResponseEntity.ok(APIResponse.success("Business address updated successfully."));
    }

    @PatchMapping("/{vendorId}/approval_status")
    @PreAuthorize("(hasAuthority('ADMIN')")
    public ResponseEntity<APIResponse<?>> updateApprovalStatus(@PathVariable Long vendorId,
                                                           @RequestBody ApprovalStatus approvalStatus) {
        serviceManager.getVendorService().updateApprovalStatus(vendorId,approvalStatus);
        return ResponseEntity.ok(APIResponse.success("Vendor approval's status changed successfully."));
    }

    @PatchMapping("/{vendorId}/vendor_status")
    @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    public ResponseEntity<APIResponse<?>> updateVendorStatus(@PathVariable Long vendorId,
                                                               @RequestBody VendorStatus vendorStatus) {
        serviceManager.getVendorService().updateVendorStatus(vendorId,vendorStatus);
        return ResponseEntity.ok(APIResponse.success("Vendor status changed successfully."));
    }

    @Operation(description = "Assign Categories")
    @PatchMapping("/{vendorId}/categories")
    @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    public ResponseEntity<APIResponse<?>> assignCategories(@PathVariable Long userId,
                                                                @RequestBody Long[] categoryIds) {
        return ResponseEntity.ok(APIResponse.success("Categories assigned successfully."));
    }

    @Operation(description = "Assign Products")
    @PatchMapping("/{vendorId}/products")
    @PreAuthorize("(hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
    public ResponseEntity<APIResponse<?>> assignProducts(@PathVariable Long vendorId,
                                                         @RequestBody Map<Long,Long[]> catProductsMap) {
        return ResponseEntity.ok(APIResponse.success("Products assigned successfully."));
    }
}
