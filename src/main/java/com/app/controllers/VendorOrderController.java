package com.app.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.config.AppConstants;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderRequest;
import com.app.payloads.OrderResponse;
import com.app.payloads.request.OrderUpdateRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.OrderUpdateResponse;
import com.app.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "9. Vendor's Order Management")@RestController
@RequestMapping("/v1/vendors/{vendorId}/orders")
@SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
@AllArgsConstructor
public class VendorOrderController {

    public final OrderService orderService;

    @Operation(summary = "Get all orders")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VENDOR')")
    @GetMapping("/")
    public ResponseEntity<APIResponse<List<OrderDTO>>> getOrdersByVendor(@PathVariable Long vendorId) {
        APIResponse<List<OrderDTO>> orders = orderService.getOrderByStoreId(vendorId);
        return new ResponseEntity<>(orders, HttpStatus.FOUND);
    }


    @Operation(summary = "Get a specific order")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VENDOR')")
    @GetMapping("/{orderId}")
    public ResponseEntity<APIResponse<OrderDTO>> getOrdersById( @PathVariable Long orderId) {
        APIResponse<OrderDTO> order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.FOUND);
    }


    @PostMapping("/")
    public ResponseEntity<APIResponse<OrderDTO>> placeOrder(@PathVariable("store_id") Long storeId,
            @RequestBody OrderRequest request) {
        APIResponse<OrderDTO> order = orderService.placeOrder(storeId, request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @Operation(summary = "Update and existing order")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VENDOR')")
    @PutMapping("/{orderId}")
    public ResponseEntity<APIResponse<OrderUpdateResponse>> updateOrder(
            @PathVariable Long orderId, @RequestBody OrderUpdateRequest request) {
        return new ResponseEntity<>(orderService.updateOrder(orderId, request), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VENDOR')")
    @GetMapping("/admin/orders")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.FOUND);
    }

    @GetMapping("public/users/{userId}/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(userId);

        return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.FOUND);
    }

    @GetMapping("public/users/{emailId}/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrderByUser(@PathVariable String emailId, @PathVariable Long orderId) {
        OrderDTO order = orderService.getOrder(emailId, orderId);

        return new ResponseEntity<OrderDTO>(order, HttpStatus.FOUND);
    }
}
