package com.app.services;

import com.app.entites.Subscription;
import com.app.entites.type.OrderStatus;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderRequest;
import com.app.payloads.OrderResponse;
import com.app.payloads.request.OrderUpdateRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.OrderUpdateResponse;
import java.util.List;

public interface OrderService {

  APIResponse<OrderDTO> placeOrder(Long storeId, OrderRequest request);

  APIResponse<OrderUpdateResponse> updateOrder(Long orderId, OrderUpdateRequest request);

  APIResponse<OrderDTO> getOrderById(final Long orderId);

  APIResponse<List<OrderDTO>> getOrderByStoreId(final Long storeId);

  OrderDTO getOrder(String emailId, Long orderId);

  List<OrderDTO> getOrdersByUser(Long userId);

  OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

  OrderDTO updateOrder(String emailId, Long orderId, OrderStatus orderStatus);

  void createInitialOrder(Subscription subscription);

  void createOrderFromSubscription(Subscription subscription);

  List<OrderDTO> getOrdersByUserId(final Long userId);
}
