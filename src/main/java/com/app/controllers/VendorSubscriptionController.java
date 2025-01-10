package com.app.controllers;

import com.app.config.AppConstants;
import com.app.controllers.validator.AbstractRequestValidation;
import com.app.payloads.request.SubscriptionStatusRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.SubscriptionResponse;
import com.app.services.SubscriptionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
@Tag(
    name = "7. Vendor Subscription API",
    description = "APIs for viewing/managing vendor subscriptions.")
@RestController
@RequestMapping("/v1/vendors")
@Slf4j
@RequiredArgsConstructor
public class VendorSubscriptionController extends AbstractRequestValidation {

  private final SubscriptionQueryService subscriptionQueryService;

  @Operation(summary = "Fetch Subscriptions By Vendor ID")
  @GetMapping("/{vendorId}/subs")
  @PreAuthorize(
      "#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('VENDOR'))")
  public ResponseEntity<APIResponse<?>> fetchSubsByVendorId(@PathVariable Long vendorId) {
    var subscriptions = subscriptionQueryService.fetchSubsByVendor(vendorId);
    return ResponseEntity.ok(APIResponse.success(subscriptions));
  }

  @PreAuthorize(
      "#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('VENDOR'))")
  @PatchMapping("/{subId}/status")
  @Operation(description = "Update subscription status")
  public ResponseEntity<APIResponse<?>> updateSubscriptionStatus(
      @PathVariable Long userId,
      @PathVariable Long subId,
      @Valid @RequestBody SubscriptionStatusRequest request) {
    SubscriptionResponse subscription =
        subscriptionQueryService.updateSubscriptionStatus(userId, subId, request.getStatus());
    return new ResponseEntity<>(APIResponse.success(subscription), HttpStatus.NO_CONTENT);
  }
}
