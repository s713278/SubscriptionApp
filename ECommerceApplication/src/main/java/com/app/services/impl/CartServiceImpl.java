package com.app.services.impl;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Order;
import com.app.entites.OrderItem;
import com.app.entites.Sku;
import com.app.entites.Store;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CartDTO;
import com.app.payloads.CartItemDTO;
import com.app.payloads.SkuDTO;
import com.app.payloads.request.ItemRequest;
import com.app.payloads.response.AddItemResponse;
import com.app.payloads.response.ApiResponse;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.SkuRepo;
import com.app.repositories.StoreRepo;
import com.app.services.CartService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Transactional
@Service
public class CartServiceImpl implements CartService {

  private CartRepo cartRepo;

  private CartItemRepo cartItemRepo;

  private ModelMapper modelMapper;

  private SkuRepo skuRepo;

  private StoreRepo storeRepo;

  @Override
  public ApiResponse<CartDTO> addOrUpdateItem(
      Long storeId, Long cartId, Long skuId, Integer quantity) {
    Store store =
        storeRepo
            .findById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", storeId));

    Sku sku =
        skuRepo
            .findByIdAndStoreId(skuId, storeId)
            .orElseThrow(() -> new ResourceNotFoundException("Sku", "skuId", skuId));

    if (sku.getStore().getId() != storeId && sku.getId() == skuId) {
      throw new APIException(
          "The item " + sku.getName() + "is not avaiable @ store " + store.getName() + ".");
    }
    if (sku.getQuantity() == 0 && sku.getStore().getId() == storeId) {
      throw new APIException(sku.getName() + " is not out of stack at " + sku.getStore().getName());
    }

    if (quantity > sku.getQuantity() && sku.getStore().getId() == storeId) {
      throw new APIException(
          "Please, make an order of the "
              + sku.getName()
              + " less than or equal to the quantity "
              + sku.getQuantity()
              + ".");
    }

    // Retrieve Existing Cart if any
    Cart shoppingCart =
        cartRepo
            .findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

    CartItem existingItem =
        cartItemRepo.findCartItemBySkuIdAndCartIdAndSkuId(skuId, cartId, storeId);
    if (existingItem != null) {
      existingItem.setQuantity(existingItem.getQuantity() + quantity);
      existingItem.setAmount(existingItem.getQuantity() * sku.getSalePrice());
      // Reducing the quantity
      // sku.setQuantity(sku.getQuantity() - quantity);
    } else {
      existingItem = new CartItem();
      existingItem.setSku(sku);
      existingItem.setCart(shoppingCart);
      existingItem.setQuantity(quantity);
      existingItem.setAmount(quantity * sku.getSalePrice());
    }
    existingItem.setUnitPrice(sku.getSalePrice());
    existingItem.setDiscount(quantity * (sku.getListPrice() - sku.getSalePrice()));
    cartItemRepo.saveAndFlush(existingItem);

    Double totalAmount =
        shoppingCart.getCartItems().stream()
            .map(cartItem1 -> cartItem1.getQuantity() * cartItem1.getUnitPrice())
            .mapToDouble(Double::doubleValue)
            .sum();
    shoppingCart.setTotalPrice(totalAmount);
    // shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() + (cartItem.getUnitPrice() *
    // quantity));

    // New CartItemDTO
    CartItemDTO cartItemDTO = modelMapper.map(existingItem, CartItemDTO.class);
    CartDTO cartDTO = modelMapper.map(shoppingCart, CartDTO.class);
    List<CartItemDTO> cartItemDTOs =
        shoppingCart.getCartItems().stream()
            .map(cartItemEntity -> modelMapper.map(cartItemEntity, CartItemDTO.class))
            .collect(Collectors.toList());
    cartItemDTOs.add(cartItemDTO);
    //  cartDTO.setItems(cartItemDTOs);
    cartRepo.saveAndFlush(shoppingCart);
    return ApiResponse.success(cartDTO);
  }

  @Override
  public List<CartDTO> getAllCarts() {
    List<Cart> carts = cartRepo.findAll();

    if (carts.size() == 0) {
      throw new APIException("No cart exists");
    }

    List<CartDTO> cartDTOs =
        carts.stream()
            .map(
                cart -> {
                  CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

                  List<SkuDTO> skus =
                      cart.getCartItems().stream()
                          .map(p -> modelMapper.map(p.getSku(), SkuDTO.class))
                          .collect(Collectors.toList());
                  // cartDTO.setSkus(skus);
                  return cartDTO;
                })
            .collect(Collectors.toList());
    return cartDTOs;
  }

  @Override
  public CartDTO getCart(String emailId, Long cartId) {
    Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);

    if (cart == null) {
      throw new ResourceNotFoundException("Cart", "cartId", cartId);
    }
    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
    List<SkuDTO> skus =
        cart.getCartItems().stream()
            .map(p -> modelMapper.map(p.getSku(), SkuDTO.class))
            .collect(Collectors.toList());
    // cartDTO.setSkus(skus);
    return cartDTO;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public ApiResponse<String> delteItem(Long cartId, Long cartItemId) {
    Cart cart =
        cartRepo
            .findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

    CartItem matchedCartITem =
        cart.getCartItems().stream()
            .filter(t -> t.getCartItemId() == cartItemId)
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("CartItem", "cartItemId", cartItemId));
    cartItemRepo.deleteBycartItemIdAndCartId(cartItemId, cartId);
    if (cart.getCartItems().isEmpty()) {
      cart.setTotalPrice(0D);
    } else {
      Double totalAmount =
          cart.getCartItems().stream()
              .map(cartItem1 -> cartItem1.getQuantity() * cartItem1.getUnitPrice())
              .mapToDouble(Double::doubleValue)
              .sum();
      cart.setTotalPrice(totalAmount);
    }
    cartItemRepo.flush();
    return ApiResponse.success(
        "Item " + matchedCartITem.getSku().getName() + " removed from the cart !!!");
  }

  @Override
  public CartDTO updateCart(Long cartId, Long skuId, Integer quantity) {
    Cart cart =
        cartRepo
            .findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

    Sku sku =
        skuRepo
            .findById(skuId)
            .orElseThrow(() -> new ResourceNotFoundException("Sku", "skuId", skuId));
    CartItem cartItem = cartItemRepo.findCartItemBySkuIdAndCartIdAndSkuId(skuId, 1L, 1L);

    if (cartItem != null) {
      throw new APIException("Sku " + sku.getName() + " already exists in the cart");
    }
    if (sku.getQuantity() == 0) {
      throw new APIException(sku.getName() + " is not available");
    }
    if (sku.getQuantity() < quantity) {
      throw new APIException(
          "Please, make an order of the "
              + sku.getName()
              + " less than or equal to the quantity "
              + sku.getQuantity()
              + ".");
    }
    CartItem newCartItem = new CartItem();
    newCartItem.setSku(sku);
    //	newCartItem.setCart(cart);
    newCartItem.setQuantity(quantity);
    newCartItem.setDiscount(sku.getSalePrice() - sku.getListPrice());
    newCartItem.setUnitPrice(sku.getSalePrice());
    cartItemRepo.save(newCartItem);
    cart.setTotalPrice(cart.getTotalPrice() + (sku.getSalePrice() * quantity));
    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
    List<SkuDTO> skuDTOs =
        cart.getCartItems().stream()
            .map(p -> modelMapper.map(p.getSku(), SkuDTO.class))
            .collect(Collectors.toList());
    // cartDTO.setSkus(skuDTOs);
    return cartDTO;
  }

  @Override
  public void updateProductInCarts(Long cartId, Long productId) {
    // TODO Auto-generated method stub

  }

  @Override
  public AddItemResponse addItem(ItemRequest request) {

    Order order = new Order();
    order.setOrderStatus("CREATED");

    OrderItem orderItem = new OrderItem();
    orderItem.setQuantity(request.getQuantity());
    order.setItems(null);

    return new AddItemResponse();
  }
}
