package com.app.services;

import com.app.payloads.CartDTO;
import com.app.payloads.request.ItemRequest;
import com.app.payloads.response.ApiResponse;
import java.util.List;

public interface CartService {

  ApiResponse<CartDTO> addOrUpdateItem(final Long storeId, final ItemRequest request);

  List<CartDTO> getAllCarts();

  ApiResponse<CartDTO> getCart(Long cartId);

  CartDTO updateCart(Long cartId, Long skuId, Integer quantity);

  void updateProductInCarts(Long cartId, Long skuId);

  ApiResponse<String> delteItem(Long cartId, Long cartItemId);

  ApiResponse<List<CartDTO>> getAllCarts(final Long storeId);
}
