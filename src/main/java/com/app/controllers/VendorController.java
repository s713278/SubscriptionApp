package com.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.config.AppConstants;
import com.app.payloads.VendorDetailsDTO;
import com.app.payloads.response.APIResponse;
import com.app.services.ServiceManager;

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

    @SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
    @PostMapping("/")
    public ResponseEntity<APIResponse<VendorDetailsDTO>> createStore(@Valid @RequestBody VendorDetailsDTO storeDTO) {
        return new ResponseEntity<>(serviceManager.getVendorService().createVendor(storeDTO), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
    @PutMapping("/{vendorId}")
    public ResponseEntity<APIResponse<VendorDetailsDTO>> updateStore(@RequestBody VendorDetailsDTO storeDTO,
                                                                     @PathVariable Long vendorId) {
        return new ResponseEntity<>(serviceManager.getVendorService().updateStore(storeDTO, vendorId), HttpStatus.OK);
    }

    @SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
    @DeleteMapping("/{vendorId}")
    public ResponseEntity<APIResponse<String>> deleteStore(@PathVariable Long vendorId) {
        return new ResponseEntity<>(serviceManager.getVendorService().deleteVendor(vendorId), HttpStatus.OK);
    }

}
