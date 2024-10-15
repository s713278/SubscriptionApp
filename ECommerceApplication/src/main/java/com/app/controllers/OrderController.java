package com.app.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.config.AppConstants;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderRequest;
import com.app.payloads.OrderResponse;
import com.app.payloads.request.OrderUpdateRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.OrderUpdateResponse;
import com.app.services.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "4. Order Management")
@RestController
@RequestMapping("/store/{store_id}")
@SecurityRequirement(name = "E-Commerce Application")
@AllArgsConstructor
public class OrderController {

    public final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<APIResponse<OrderDTO>> placeOrder(@PathVariable("store_id") Long storeId,
            @RequestBody OrderRequest request) {

        APIResponse<OrderDTO> order = orderService.placeOrder(storeId, request);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STORE')")
    @PutMapping("/orders/{order_id}")
    public ResponseEntity<APIResponse<OrderUpdateResponse>> updateOrder(@PathVariable("store_id") Long storeId,
            @PathVariable("order_id") Long orderId, @RequestBody OrderUpdateRequest request) {
        return new ResponseEntity<>(orderService.updateOrder(orderId, request), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STORE') or hasAuthority('USER')")
    @GetMapping("/orders/{order_id}")
    public ResponseEntity<APIResponse<OrderDTO>> getOrdersById(@PathVariable("store_id") Long storeId,
            @PathVariable("order_id") Long orderId) {
        APIResponse<OrderDTO> order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.FOUND);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STORE')")
    @GetMapping("/orders")
    public ResponseEntity<APIResponse<List<OrderDTO>>> getOrdersByStoreId(@PathVariable("store_id") Long storeId) {
        APIResponse<List<OrderDTO>> orders = orderService.getOrderByStoreId(storeId);
        return new ResponseEntity<>(orders, HttpStatus.FOUND);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STORE')")
    @GetMapping("/admin/orders")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.FOUND);
    }

    @GetMapping("public/users/{emailId}/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String emailId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(emailId);

        return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.FOUND);
    }

    @GetMapping("public/users/{emailId}/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrderByUser(@PathVariable String emailId, @PathVariable Long orderId) {
        OrderDTO order = orderService.getOrder(emailId, orderId);

        return new ResponseEntity<OrderDTO>(order, HttpStatus.FOUND);
    }
}
