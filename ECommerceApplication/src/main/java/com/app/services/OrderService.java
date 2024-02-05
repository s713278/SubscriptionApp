package com.app.services;

import com.app.payloads.OrderDTO;
import com.app.payloads.OrderRequest;
import com.app.payloads.OrderResponse;
import com.app.payloads.request.OrderUpdateRequest;
import com.app.payloads.response.ApiResponse;
import com.app.payloads.response.OrderUpdateResponse;
import com.app.services.constants.OrderStatus;
import java.util.List;

public interface OrderService {

    ApiResponse<OrderDTO> placeOrder(Long storeId, OrderRequest request);

    OrderDTO getOrder(String emailId, Long orderId);

    List<OrderDTO> getOrdersByUser(String emailId);

    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ApiResponse<OrderUpdateResponse> updateOrder(Long orderId, OrderUpdateRequest request);

    OrderDTO updateOrder(String emailId, Long orderId, OrderStatus orderStatus);
}
