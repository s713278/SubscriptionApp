package com.app.services.impl;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Order;
import com.app.entites.OrderItem;
import com.app.entites.OrderStatusHistory;
import com.app.entites.Payment;
import com.app.entites.Shipping;
import com.app.entites.Store;
import com.app.entites.User;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderRequest;
import com.app.payloads.OrderResponse;
import com.app.payloads.request.OrderUpdateRequest;
import com.app.payloads.response.ApiResponse;
import com.app.payloads.response.OrderUpdateResponse;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.OrderItemRepo;
import com.app.repositories.OrderRepo;
import com.app.repositories.PaymentRepo;
import com.app.repositories.StoreRepo;
import com.app.repositories.UserRepo;
import com.app.services.CartService;
import com.app.services.OrderService;
import com.app.services.UserService;
import com.app.services.constants.OrderStatus;
import com.app.services.constants.PaymentType;
import com.app.services.constants.ShippingType;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl extends AbstarctCatalogService implements OrderService {

    public OrderServiceImpl(UserRepo userRepo, CartRepo cartRepo, OrderRepo orderRepo, PaymentRepo paymentRepo,
            OrderItemRepo orderItemRepo, CartItemRepo cartItemRepo, UserService userService, CartService cartService,
            StoreRepo storeRepo, ModelMapper modelMapper) {
        super(userRepo, cartRepo, orderRepo, paymentRepo, orderItemRepo, cartItemRepo, userService, cartService, storeRepo,
                modelMapper);
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {Exception.class, APIException.class, ResourceNotFoundException.class})
    @Override
    public ApiResponse<OrderDTO> placeOrder(final Long storeId, final OrderRequest request) {
        final Long userId = request.getUserId();
        final Long cartId = request.getCartId();
        final User user = validateUser(userId);
        Cart cart = validateCart(cartId, user);
        final Store store = validateCartItemsAndStore(storeId, cart);
        Order order = createOrder(request, user, cart, store);
        processOrderItems(cart, order);
        // Update inventory
        updateCartAndSkuQuantities(cart);
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        order = orderRepo.saveAndFlush(order);
        orderDTO.setOrderId(order.getOrderId());
        return ApiResponse.success(orderDTO);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(String emailId) {
        List<Order> orders = orderRepo.findAllByEmail(emailId);

        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());

        if (orderDTOs.size() == 0) {
            throw new APIException("No orders placed yet by the user with email: " + emailId);
        }

        return orderDTOs;
    }

    @Override
    public OrderDTO getOrder(String emailId, Long orderId) {

        Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);

        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderId", orderId);
        }

        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Order> pageOrders = orderRepo.findAll(pageDetails);

        List<Order> orders = pageOrders.getContent();

        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());

        if (orderDTOs.size() == 0) {
            throw new APIException("No orders placed yet by the users");
        }

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());

        return orderResponse;
    }

    @Override
    public ApiResponse<OrderUpdateResponse> updateOrder(Long orderId, OrderUpdateRequest request) {
        Order order = orderRepo
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));
        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderId", orderId);
        }
        OrderStatus newOrderStatus = OrderStatus.valueOf(request.getNewSatus().toUpperCase());
        order.setOrderStatus(newOrderStatus);
        OrderStatusHistory orderStatusHistory = createOrderStatusHistory(order);
        return ApiResponse.success(modelMapper.map(OrderStatusHistory.class, OrderUpdateResponse.class));
    }

    @Override
    public OrderDTO updateOrder(String emailId, Long orderId, OrderStatus orderStatus) {
        Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderId", orderId);
        }
        order.setOrderStatus(orderStatus);
        return modelMapper.map(order, OrderDTO.class);
    }

    /**
     * Create and populate the Order entity
     *
     * @param storeId
     * @param request
     * @param user
     * @param cart
     * @return
     */
    private Order createOrder(OrderRequest request, User user, Cart cart, Store store) {
        Order order = new Order();
        order.setEmail(user.getEmail());
        order.setOrderTime(Instant.now());
        order.setOrderStatus(OrderStatus.CREATED);
        order.setFederalTax(cart.getTotalPrice() * 0.2);
        order.setStateTax(cart.getTotalPrice() * 0.5);
        order.setSubTotal(cart.getTotalPrice());
        order.setTotalAmount(cart.getTotalPrice() + order.getFederalTax() + order.getStateTax());

        order.setUser(user);
        order.setStore(store);
        createPayment(request, order);
        createShipping(request, order);
        return order;
    }

    private void processOrderItems(Cart cart, Order order) {
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> createOrderItem(cartItem, order))
                .collect(Collectors.toList());
        order.setItems(orderItems);
    }

    private OrderItem createOrderItem(CartItem cartItem, Order order) {
        // Create and populate the OrderItem entity
        OrderItem orderItem = new OrderItem();
        orderItem.setSku(cartItem.getSku());
        orderItem.setUnitPrice(cartItem.getUnitPrice());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setAmount(cartItem.getQuantity() * cartItem.getUnitPrice());
        orderItem.setDiscount(cartItem.getDiscount());
        orderItem.setOrder(order);
        return orderItem;
    }

    private void createPayment(final OrderRequest request, Order order) {
        Payment payment = new Payment();
        payment = modelMapper.map(request.getPaymentDetails(), Payment.class);
        payment.setOrder(order);
        PaymentType paymentType = PaymentType.valueOf(request.getPaymentDetails().getPaymentMethod());
        payment.setPaymentMethod(paymentType);
        // payment = paymentRepo.save(payment);
        order.setPayment(payment);
    }

    private void createShipping(final OrderRequest request, Order order) {
        Shipping shipping = modelMapper.map(request.getShippingDetails(), Shipping.class);
        ShippingType shippingType =
                ShippingType.valueOf(request.getShippingDetails().getShippingMethod());
        shipping.setShippingMethod(shippingType);
        shipping.setOrder(order);
        // Shipping
        order.setShipping(shipping);
    }

    private OrderStatusHistory createOrderStatusHistory(Order order) {
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setOldStatus(order.getOrderStatus());
        orderStatusHistory.setNewStatus(OrderStatus.CREATED);
        orderStatusHistory.setChangedAt(LocalDateTime.now());
        return orderStatusHistory;
    }
}
