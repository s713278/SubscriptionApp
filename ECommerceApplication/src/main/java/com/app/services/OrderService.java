package com.app.services;

import com.app.entites.SubscriptionItem;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderRequest;
import com.app.payloads.OrderResponse;
import com.app.payloads.request.OrderUpdateRequest;
import com.app.payloads.response.AppResponse;
import com.app.payloads.response.OrderUpdateResponse;
import com.app.services.constants.OrderStatus;
import java.util.List;

public interface OrderService {

    AppResponse<OrderDTO> placeOrder(Long storeId, OrderRequest request);

    AppResponse<OrderUpdateResponse> updateOrder(Long orderId, OrderUpdateRequest request);

    AppResponse<OrderDTO> getOrderById(final Long orderId);

    AppResponse<List<OrderDTO>> getOrderByStoreId(final Long storeId);

    OrderDTO getOrder(String emailId, Long orderId);

    List<OrderDTO> getOrdersByUser(String emailId);

    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderDTO updateOrder(String emailId, Long orderId, OrderStatus orderStatus);

    void createInitialOrder(SubscriptionItem subscription);
}
