package com.app.controllers;

import com.app.config.AppConstants;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderDetailsDTO;
import com.app.payloads.request.OrderUpdateRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.OrderUpdateResponse;
import com.app.services.OrderService;
import com.app.services.UserOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "4. Order API", description = "APIs for viewing and managing system generated orders.")
@RestController
@RequestMapping("/v1/users/{userId}/orders")
@SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
@AllArgsConstructor
public class UserOrderController {

  public final OrderService orderService;
  public final UserOrderService userOrderService;

  @Operation(summary = "History", description = "Accessed by logged in user only.")
  @PreAuthorize(value = "#userId==authentication.principal")
  @GetMapping("/history")
  public ResponseEntity<APIResponse<?>> getUserOrderHistory(@PathVariable Long userId) {
    var history = userOrderService.getUserOrderHistory(userId);
    return ResponseEntity.ok(APIResponse.success(history));
  }

  @PreAuthorize(value = "#userId==authentication.principal")
  @GetMapping("/history/date-range")
  public ResponseEntity<List<OrderDetailsDTO>> getOrderHistoryByDateRange(
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam("userId") Long userId) {

    List<OrderDetailsDTO> orderHistory =
        userOrderService.getOrderHistoryByDateRange(userId, startDate, endDate);
    return ResponseEntity.ok(orderHistory);
  }

  @PreAuthorize(value = "#userId==authentication.principal")
  @GetMapping("/history/excel")
  public ResponseEntity<Resource> downloadOrderHistory(
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam("userId") Long userId) {

    Resource file = userOrderService.exportOrderHistoryToCsv(userId, startDate, endDate);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order-history.csv")
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }

  @PostMapping("/history/email")
  public ResponseEntity<String> emailOrderHistory(
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam("userId") Long userId,
      @RequestParam("email") String email) {

    userOrderService.sendOrderHistoryByEmail(userId, startDate, endDate, email);
    return ResponseEntity.ok("Order history sent to " + email);
  }

  @GetMapping("/history/download-pdf")
  public ResponseEntity<Resource> downloadOrderHistoryPdf(
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam("userId") Long userId) {

    Resource pdfResource = userOrderService.exportOrderHistoryToPdf(userId, startDate, endDate);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order-history.pdf")
        .contentType(MediaType.parseMediaType("application/pdf"))
        .body(pdfResource);
  }

  @Operation(summary = "Get all orders")
  // @GetMapping("/")
  public ResponseEntity<?> getAllOrders(@PathVariable Long userId) {
    var orders = orderService.getOrdersByUser(userId);
    return ResponseEntity.ok(APIResponse.success(orders));
  }

  @Operation(summary = "Get a specific order")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VENDOR') or hasAuthority('USER')")
  //  @GetMapping("/{orderId}")
  public ResponseEntity<APIResponse<OrderDTO>> getOrdersById(@PathVariable Long orderId) {
    APIResponse<OrderDTO> order = orderService.getOrderById(orderId);
    return new ResponseEntity<>(order, HttpStatus.FOUND);
  }

  @Operation(summary = "Update and existing order")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VENDOR')")
  // @PutMapping("/{orderId}")
  public ResponseEntity<APIResponse<OrderUpdateResponse>> updateOrder(
      @PathVariable Long orderId, @RequestBody OrderUpdateRequest request) {
    return new ResponseEntity<>(orderService.updateOrder(orderId, request), HttpStatus.OK);
  }
}
