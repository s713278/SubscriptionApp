package com.app.controllers;

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
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "9. Vendor's Order API", description = "APIs for vendor's order management.")
@RestController
@RequestMapping("/v1/vendors/{vendor_id}/orders")
@SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
@AllArgsConstructor
public class VendorOrderController {

  public final OrderService orderService;

  @Operation(summary = "Get all orders")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VENDOR')")
  @GetMapping("/")
  public ResponseEntity<APIResponse<List<OrderDTO>>> getOrdersByVendor(
      @PathVariable("vendor_id") Long vendorId) {
    APIResponse<List<OrderDTO>> orders = orderService.getOrderByStoreId(vendorId);
    return new ResponseEntity<>(orders, HttpStatus.FOUND);
  }

  @Operation(summary = "Get a specific order")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VENDOR')")
  @GetMapping("/{order_id}")
  public ResponseEntity<APIResponse<OrderDTO>> getOrdersById(
      @PathVariable("order_id") Long orderId) {
    APIResponse<OrderDTO> order = orderService.getOrderById(orderId);
    return new ResponseEntity<>(order, HttpStatus.FOUND);
  }

  @PostMapping("/")
  public ResponseEntity<APIResponse<OrderDTO>> placeOrder(
      @PathVariable(("vendor_id")) Long storeId, @RequestBody OrderRequest request) {
    APIResponse<OrderDTO> order = orderService.placeOrder(storeId, request);
    return new ResponseEntity<>(order, HttpStatus.CREATED);
  }

  @Operation(summary = "Update and existing order")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VENDOR')")
  @PutMapping("/{order_id}")
  public ResponseEntity<APIResponse<OrderUpdateResponse>> updateOrder(
      @PathVariable("order_id") Long orderId, @RequestBody OrderUpdateRequest request) {
    return new ResponseEntity<>(orderService.updateOrder(orderId, request), HttpStatus.OK);
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VENDOR')")
  @GetMapping("/admin/orders")
  public ResponseEntity<OrderResponse> getAllOrders(
      @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)
          Integer pageNumber,
      @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)
          Integer pageSize,
      @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false)
          String sortBy,
      @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false)
          String sortOrder) {
    OrderResponse orderResponse =
        orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
    return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
  }

  @GetMapping("public/users/{user_id}/orders")
  public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable("user_id") Long userId) {
    List<OrderDTO> orders = orderService.getOrdersByUser(userId);

    return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.OK);
  }

  @GetMapping("public/users/{email_id}/orders/{order_id}")
  public ResponseEntity<OrderDTO> getOrderByUser(
      @PathVariable("email_id") String emailId, @PathVariable("order_id") Long orderId) {
    OrderDTO order = orderService.getOrder(emailId, orderId);

    return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
  }
}
