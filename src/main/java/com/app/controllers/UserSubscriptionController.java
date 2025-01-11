package com.app.controllers;

import com.app.config.AppConstants;
import com.app.controllers.validator.AbstractRequestValidation;
import com.app.entites.Subscription;
import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.request.SubscriptionStatusRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.SubscriptionResponse;
import com.app.services.AbstractSubscriptionCreateService;
import com.app.services.SubscriptionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
@Tag(
    name = "3. Subscription API",
    description = "APIS for creating ,managing user subscriptions after sign-in.")
@RestController
@RequestMapping("/v1/users/{user_id}/subs")
@Slf4j
@RequiredArgsConstructor
public class UserSubscriptionController extends AbstractRequestValidation {

  private final SubscriptionQueryService subscriptionQueryService;
  private final AbstractSubscriptionCreateService createSubscriptionService;

  @PreAuthorize(
      "#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDOR'))")
  @Operation(
      summary = "Create Subscription",
      description = "API accessed through Bearer Token only.")
  @PostMapping("/")
  public ResponseEntity<APIResponse<?>> createSubscription(
      @Schema(example = "3302") @PathVariable("user_id") Long userId,
      @Valid @RequestBody CreateSubscriptionRequest request,
      BindingResult bindingResult) {
    validateRequest(bindingResult);
    log.debug("Entered create subscription for customer {}", userId);
    var responseDTO = createSubscriptionService.createSubscription(userId, request);
    // Return the response wrapped in ResponseEntity with HTTP status 201 (Created)
    return new ResponseEntity<>(APIResponse.success(responseDTO), HttpStatus.CREATED);
  }

  @PreAuthorize(
      "#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDOR'))")
  @PatchMapping("/{sub_id}")
  @Operation(summary = "Update Subscription")
  public ResponseEntity<APIResponse<?>> updateSubscription(
      @PathVariable("user_id") Long userId,
      @PathVariable("sub_id") Long subId,
      @Valid @RequestBody UpdateSubscriptionRequest request,
      BindingResult bindingResult) {
    validateRequest(bindingResult);
    SubscriptionResponse subscription =
        subscriptionQueryService.updateSubscription(userId, subId, request);
    return new ResponseEntity<>(APIResponse.success(subscription), HttpStatus.OK);
  }

  @PreAuthorize(
      "#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDOR'))")
  @PatchMapping("/{sub_id}/status")
  @Operation(description = "Update subscription status")
  public ResponseEntity<APIResponse<?>> updateSubscriptionStatus(
      @PathVariable("user_id") Long userId,
      @PathVariable("sub_id") Long subId,
      @Valid @RequestBody SubscriptionStatusRequest request) {
    SubscriptionResponse subscription =
        subscriptionQueryService.updateSubscriptionStatus(userId, subId, request.getStatus());
    return new ResponseEntity<>(APIResponse.success(subscription), HttpStatus.NO_CONTENT);
  }

  @PreAuthorize(
      "#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDOR'))")
  @GetMapping("/{sub_id}")
  @Operation(
      summary = "Fetch subscription ",
      description = "Accessed by signin user with bearer token only.")
  public ResponseEntity<Subscription> fetchSubscription(
      @Schema(example = "2552") @PathVariable("sub_id") Long subId,
      @Schema(example = "3302") @PathVariable("user_id") Long userId) {
    var sub = subscriptionQueryService.fetchSubscription(subId);
    return ResponseEntity.ok(sub);
  }

  /* @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDOR'))")
  @DeleteMapping("/{subId}")
  @Operation(summary  = "Delete Subscription")
  public ResponseEntity<APIResponse<String>> deleteSubscription( @PathVariable Long subId,@PathVariable Long userId) {
       subscriptionService.updateSubscriptionStatus(subId, userId,SubscriptionStatus.DELETED);
      //subscriptionService.deleteSubscription(subId);
      return new ResponseEntity<>(APIResponse.success(HttpStatus.NO_CONTENT.value(),"Subscription deleted successfully"),HttpStatus.NO_CONTENT);
  }*/

  @Operation(summary = "Fetch All Subscriptions")
  @GetMapping("/")
  @PreAuthorize(
      "#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER'))")
  public ResponseEntity<APIResponse<?>> fetchAllSubsByUserId(@PathVariable("user_id") Long userId) {
    var subscriptions = subscriptionQueryService.fetchSubsByUserId(userId);
    return ResponseEntity.ok(APIResponse.success(subscriptions));
  }

  @Operation(summary = "Fetch Subscriptions By Vendor and User")
  @GetMapping("/vendor/{vendor_id}")
  @PreAuthorize(
      "#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER'))")
  public ResponseEntity<APIResponse<?>> fetchSubsByUserAndVendor(
      @PathVariable("vendor_id") Long vendorId, @PathVariable("user_id") Long userId) {
    var subscriptions = subscriptionQueryService.fetchSubsByUserAndVendor(userId, vendorId);
    return ResponseEntity.ok(APIResponse.success(subscriptions));
  }
}
