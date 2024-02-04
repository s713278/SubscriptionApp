package com.app.services;

import com.app.payloads.OrderDTO;
import com.app.payloads.OrderRequest;
import com.app.payloads.OrderResponse;
import com.app.payloads.response.ApiResponse;
import java.util.List;

public interface OrderService {

  ApiResponse<OrderDTO> placeOrder(Long storeId, OrderRequest request);

  OrderDTO getOrder(String emailId, Long orderId);

  List<OrderDTO> getOrdersByUser(String emailId);

  OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

  OrderDTO updateOrder(String emailId, Long orderId, String orderStatus);
}
