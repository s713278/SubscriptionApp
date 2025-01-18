package com.app.controllers;

import com.app.config.AppConstants;
import com.app.payloads.response.APIResponse;
import com.app.services.ServiceManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class VendorQueryController {

  private final ServiceManager serviceManager;

  @Operation(
      summary = "Vendor assigned categories",
      description =
          """
                      This API fetches assigned categories and This API accessed by Vendor/Admin/Customer_Care role.
                      <br> This response.data.id has to be used for assigning products to vendor.
                      """)
  @GetMapping("/{vendor_id}/categories")
  @PreAuthorize(
      "(hasAuthority('VENDOR') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER_CARE'))")
  public ResponseEntity<APIResponse<?>> fetchAssignCategories(
      @PathVariable("vendor_id") Long vendorId) {
    var response = serviceManager.getVendorService().fetchAssignedCategories(vendorId);
    return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
  }
}
