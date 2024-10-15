package com.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entites.Subscription;
import com.app.payloads.request.SubscriptionRequest;
import com.app.payloads.request.SubscriptionStatusRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.payloads.response.SubscriptionResponse;
import com.app.services.AbstractCreateSubscriptionService;
import com.app.services.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Tag(name = "3. Subscription Management")
@RestController
@RequestMapping("/vendor/{vendorId}/subscription")
@Slf4j
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final AbstractCreateSubscriptionService createSubscriptionService;

    //@SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
    @Operation(description = "Create a new subscription")
    @PostMapping("/")
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @PathVariable Long vendorId,
            @Valid @RequestBody SubscriptionRequest request) {
        request.setVendorId(vendorId);
        log.debug("Entered create subscription for customer {}",request.getCustomerId());
        SubscriptionResponse subscription = createSubscriptionService.createSubscription(request);
     // Return the response wrapped in ResponseEntity with HTTP status 201 (Created)
        return new ResponseEntity<>(subscription, HttpStatus.CREATED);
    }


    @PatchMapping("/{subId}")
    @Operation(description  = "Update an existing subscription")
    public ResponseEntity<SubscriptionResponse> updateSubscription(@PathVariable Long vendorId,@PathVariable Long subId,
            @Valid @RequestBody UpdateSubscriptionRequest request) {
        request.setSubscriptionId(subId);
        request.setVendorId(vendorId);
        SubscriptionResponse subscription =subscriptionService.updateSubscription(request);
        return new ResponseEntity<>(subscription,HttpStatus.OK);
    }
    
    @PatchMapping("/{subId}/status")
    @Operation(description  = "Update subscription status")
    public ResponseEntity<SubscriptionResponse> updateSubscriptionStatus(@PathVariable Long vendorId,
            @PathVariable Long subId,
            @Valid @RequestBody SubscriptionStatusRequest request) {
        request.setSubscriptionId(subId);
        request.setVendorId(vendorId);
        SubscriptionResponse subscription =subscriptionService.updateSubscriptionStatus(request);
        return new ResponseEntity<>(subscription,HttpStatus.OK);
    }

    
    @GetMapping("/{subId}")
    @Operation(description  = "Fetch subscription details")
    public ResponseEntity<Subscription> fetchSubscription(@PathVariable Long subId) {
        var sub = subscriptionService.fetchSubscription(subId);
        return ResponseEntity.ok(sub);
    }
    
    @DeleteMapping("/{subId}")
    @Operation(description  = "Delete a subscription by ID")
    public ResponseEntity<String> deleteSubscription(@PathVariable Long subId) {
        subscriptionService.deleteSubscription(subId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Subscription deleted successfully");
    }
}
