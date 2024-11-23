package com.app.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.config.AppConstants;
import com.app.payloads.request.NameAndAddressRequest;
import com.app.payloads.request.UpdateMobileRequest;
import com.app.payloads.request.UpdateUserRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.UserResponse;
import com.app.services.SubscriptionService;
import com.app.services.impl.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
@RestController
@Tag(name = "5. User Management")
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class CustomerController {
        
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN'))")
    @GetMapping("/admin")
    public ResponseEntity<UserResponse> getUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        UserResponse userResponse = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.FOUND);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER'))")
    @Operation(summary = "User Information")
    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse<?>> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(APIResponse.success(userService.getUserInfo(userId)));
    }


    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER'))")
    @Operation(summary = "Update User Information")
    @PutMapping("/{userId}")
    public ResponseEntity<APIResponse<?>> updateUser(@RequestBody UpdateUserRequest userDTO, @PathVariable Long userId) {
        userService.updateUser(userId, userDTO);
        return new ResponseEntity<>(APIResponse.success("User information updated successfully."), HttpStatus.OK);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER'))")
    @DeleteMapping("/{userId}")
   // @Operation(summary = "Delete User")
    public ResponseEntity<APIResponse<?>> deleteUser(@PathVariable Long userId) {
        String status = userService.deleteUser(userId);
        return new ResponseEntity<>(APIResponse.success("User is deleted successfully."), HttpStatus.OK);
    }

    @Operation(summary = "Update Delivery Address", description = "Updates the delivery address for a specific user. Only valid keys (address1, address2, city, state, zipCode, country) are accepted.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input keys or validation error", content = @Content(schema = @Schema(example = "{\"error\": \"Invalid keys found: [invalidKey]\"}"))),
            @ApiResponse(responseCode = "404", description = "User not found") })

    @io.swagger.v3.oas.annotations.parameters.RequestBody
            (description = "A map containing valid delivery address fields. Valid keys: address1, address2, city, state, zipCode, country.",
                    required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = NameAndAddressRequest.class)))
    @PatchMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER'))")
    public ResponseEntity<APIResponse<?>> addNameAndAddress(@PathVariable Long userId,
                                                            @RequestBody NameAndAddressRequest request) {
        userService.addNameAndAddress(userId, request.name(),request.address());
        return ResponseEntity.ok(APIResponse.success("Address updated successfully."));
    }

    @PatchMapping("/{userId}/del_instructions")
    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER'))")
    public ResponseEntity<APIResponse<?>> updateDeliveryInstructions(@PathVariable Long userId,
                                                        @RequestBody Map<String, String> deliveryInstructions) {
        userService.updateDeliveryInstructions(userId, deliveryInstructions);
        return ResponseEntity.ok(APIResponse.success("Address updated successfully."));
    }

    @Operation(summary = "Fetch Subscriptions By User ID")
    @GetMapping("/{userId}/subs")
    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER'))")
    public ResponseEntity<APIResponse<?>> fetchAllSubsByUserId( @PathVariable Long userId) {
        var subscriptions = subscriptionService.fetchSubsByUserId(userId);
        return ResponseEntity.ok(APIResponse.success(subscriptions));
    }

    @Operation(summary = "Fetch Subscriptions By Vendor and User")
    @GetMapping("/{userId}/vendor/{vendorId}")
    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER'))")
    public ResponseEntity<APIResponse<?>> fetchSubsByUserAndVendor( @PathVariable Long vendorId,
            @PathVariable Long userId) {
        var subscriptions = subscriptionService.fetchSubsByUserAndVendor(userId, vendorId);
        return ResponseEntity.ok(APIResponse.success(subscriptions));
    }

    @PatchMapping("/{userId}/mobile")
    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('USER'))")
    public ResponseEntity<APIResponse<?>> updateMobileAddress(@PathVariable Long userId,
                                                        @RequestBody UpdateMobileRequest request) {
        //TODO : Implement
        return ResponseEntity.ok(APIResponse.success("Mobile number updated successfully."));
    }

}
