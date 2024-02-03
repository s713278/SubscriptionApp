package com.app.services;

import com.app.payloads.CartDTO;
import com.app.payloads.request.ItemRequest;
import com.app.payloads.response.AddItemResponse;
import com.app.payloads.response.ApiResponse;
import java.util.List;

public interface CartService {

  AddItemResponse addItem(ItemRequest request);

  ApiResponse<CartDTO> addOrUpdateItem(Long storeId, Long cartId, Long skuId, Integer quantity);

  List<CartDTO> getAllCarts();

  CartDTO getCart(String emailId, Long cartId);

  CartDTO updateCart(Long cartId, Long skuId, Integer quantity);

  void updateProductInCarts(Long cartId, Long skuId);

  ApiResponse<String> delteItem(Long cartId, Long cartItemId);
}
