package com.app.controllers;

import com.app.config.AppConstants;
import com.app.entites.Subscription;
import com.app.entites.SubscriptionStatus;
import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.request.SubscriptionStatusRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.SubscriptionResponse;
import com.app.services.AbstractCreateSubscriptionService;
import com.app.services.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "3. Subscription Management")
@RestController
@RequestMapping("/v1/users/{userId}/subs")
@Slf4j
@RequiredArgsConstructor
public class SubscriptionController extends AbstractRequestValidation{

    private final SubscriptionService subscriptionService;
    private final AbstractCreateSubscriptionService createSubscriptionService;

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDOR'))")
    @Operation(summary = "Create Subscription")
    @PostMapping("/")
    public ResponseEntity<APIResponse<?>> createSubscription(
            @PathVariable Long userId,
            @Valid @RequestBody CreateSubscriptionRequest request, BindingResult bindingResult) {
        validateRequest(bindingResult);

        log.debug("Entered create subscription for customer {}",userId);
        var subscription = createSubscriptionService.createSubscription(userId,request);
     // Return the response wrapped in ResponseEntity with HTTP status 201 (Created)
        return new ResponseEntity<>(APIResponse.success(subscription),HttpStatus.CREATED);
    }


    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDOR'))")
    @PatchMapping("/{subId}")
    @Operation(summary  = "Update Subscription")
    public ResponseEntity<APIResponse<?>> updateSubscription(@PathVariable Long userId, @PathVariable Long subId,
                                                          @Valid @RequestBody UpdateSubscriptionRequest request, BindingResult bindingResult) {
        validateRequest(bindingResult);
        SubscriptionResponse subscription =subscriptionService.updateSubscription(userId,subId,request);
        return new ResponseEntity<>(APIResponse.success(subscription),HttpStatus.OK);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDOR'))")
    @PatchMapping("/{subId}/status")
    @Operation(description  = "Update subscription status")
    public ResponseEntity<APIResponse<?>> updateSubscriptionStatus(@PathVariable Long userId,
            @PathVariable Long subId,
            @Valid @RequestBody SubscriptionStatusRequest request) {
        SubscriptionResponse subscription =subscriptionService.updateSubscriptionStatus(userId,subId,request.getStatus());
        return new ResponseEntity<>(APIResponse.success(subscription),HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDOR'))")
    @GetMapping("/{subId}")
    @Operation(summary  = "Fetch Subscription Details")
    public ResponseEntity<Subscription> fetchSubscription(@PathVariable Long subId,@PathVariable Long userId) {
        var sub = subscriptionService.fetchSubscription(subId);
        return ResponseEntity.ok(sub);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDOR'))")
    @DeleteMapping("/{subId}")
    @Operation(summary  = "Delete Subscription")
    public ResponseEntity<APIResponse<String>> deleteSubscription( @PathVariable Long subId,@PathVariable Long userId) {
         subscriptionService.updateSubscriptionStatus(subId, userId,SubscriptionStatus.DELETED);
        //subscriptionService.deleteSubscription(subId);
        return new ResponseEntity<>(APIResponse.success(HttpStatus.NO_CONTENT.value(),"Subscription deleted successfully"),HttpStatus.NO_CONTENT);
    }
}
