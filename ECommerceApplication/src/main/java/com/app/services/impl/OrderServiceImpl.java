package com.app.services.impl;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Order;
import com.app.entites.OrderItem;
import com.app.entites.Payment;
import com.app.entites.Shipping;
import com.app.entites.Sku;
import com.app.entites.Store;
import com.app.entites.User;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderRequest;
import com.app.payloads.OrderResponse;
import com.app.payloads.response.ApiResponse;
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
import com.app.services.constants.OrderConstants;
import com.app.services.constants.ShippingOptions;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

  public final UserRepo userRepo;

  public final CartRepo cartRepo;

  public final OrderRepo orderRepo;

  public final PaymentRepo paymentRepo;

  public final OrderItemRepo orderItemRepo;

  public final CartItemRepo cartItemRepo;

  public final UserService userService;

  public final CartService cartService;

  public final StoreRepo storeRepo;

  public final ModelMapper modelMapper;

  @Transactional(
      propagation = Propagation.REQUIRES_NEW,
      rollbackForClassName = {"Exception"})
  @Override
  public ApiResponse<OrderDTO> placeOrder(final Long storeId, final OrderRequest request) {

    Long userId = request.getUserId();
    Long cartId = request.getCartId();
    String paymentMethod = "CreditCard";
    // Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);

    User user =
        userRepo
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

    if (user.getCart().getId().compareTo(cartId) != 0) {
      throw new APIException(
          String.format(
              "Malformed request while placing the order where cartId: %d is not belongs to userId : %d",
              cartId, userId));
    }
    Cart cart =
        cartRepo
            .findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
    List<CartItem> cartItems = cart.getCartItems();
    if (cartItems.size() == 0) {
      throw new APIException("Cart is empty");
    }

    Store store =
        storeRepo
            .findById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", storeId));
    boolean allItemsStoreMatched =
        cart.getCartItems().stream().allMatch(t -> t.getSku().getStore().getId() == storeId);

    if (!allItemsStoreMatched) {
      throw new APIException(
          String.format("Not all order items have store id % in the cart % ", storeId, cartId));
    }

    Order order = new Order();
    order.setUser(user);
    order.setStore(store);
    order.setEmail(user.getEmail());
    order.setOrderTime(Instant.now());
    order.setOrderStatus(OrderConstants.ORDER_CREATED);
    order.setFederalTax(cart.getTotalPrice() * 0.2);
    order.setStateTax(cart.getTotalPrice() * 0.5);
    order.setSubTotal(cart.getTotalPrice());
    order.setTotalAmount(cart.getTotalPrice() + order.getFederalTax() + order.getStateTax());

    // Order newOrder = orderRepo.save(order);

    List<OrderItem> orderItems = new ArrayList<>();

    for (CartItem cartItem : cartItems) {
      OrderItem orderItem = new OrderItem();

      orderItem.setSku(cartItem.getSku());
      orderItem.setUnitPrice(cartItem.getUnitPrice());
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setAmount(cartItem.getQuantity() * cartItem.getUnitPrice());
      orderItem.setDiscount(cartItem.getDiscount());
      orderItem.setOrder(order);
      orderItems.add(orderItem);
    }

    order.setItems(orderItems);

    Payment payment = new Payment();
    payment = modelMapper.map(request.getPaymentDetails(), Payment.class);
    payment.setOrder(order);
    payment.setPaymentMethod(paymentMethod);

    // payment = paymentRepo.save(payment);
    order.setPayment(payment);

    Shipping shipping = new Shipping();
    shipping = modelMapper.map(request.getShippingDetails(), Shipping.class);
    shipping.setShippingMethod(ShippingOptions.SHIPPING_STORE_PICKUP);
    shipping.setOrder(order);
    // Shipping
    order.setShipping(shipping);

    // orderItems = orderItemRepo.saveAllAndFlush(orderItems);

    cart.getCartItems()
        .forEach(
            item -> {
              int quantity = item.getQuantity();

              Sku sku = item.getSku();
              cartService.delteItem(cart.getId(), item.getCartItemId());
              sku.setQuantity(sku.getQuantity() - quantity);
            });
    cart.setTotalPrice(0D);
    OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

    /*
	 * orderItems.forEach( orderItemEntity ->
	 * orderDTO.getItems().add(modelMapper.map(orderItemEntity,
	 * OrderItemDTO.class)));
	 */ order = orderRepo.saveAndFlush(order);
    orderDTO.setOrderId(order.getOrderId());
    return ApiResponse.success(orderDTO);
  }

  @Override
  public List<OrderDTO> getOrdersByUser(String emailId) {
    List<Order> orders = orderRepo.findAllByEmail(emailId);

    List<OrderDTO> orderDTOs =
        orders.stream()
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
  public OrderResponse getAllOrders(
      Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

    Sort sortByAndOrder =
        sortOrder.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

    Page<Order> pageOrders = orderRepo.findAll(pageDetails);

    List<Order> orders = pageOrders.getContent();

    List<OrderDTO> orderDTOs =
        orders.stream()
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
  public OrderDTO updateOrder(String emailId, Long orderId, String orderStatus) {

    Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);

    if (order == null) {
      throw new ResourceNotFoundException("Order", "orderId", orderId);
    }

    order.setOrderStatus(orderStatus);

    return modelMapper.map(order, OrderDTO.class);
  }
}
