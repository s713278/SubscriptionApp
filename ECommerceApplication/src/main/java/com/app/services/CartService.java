package com.app.services;

import java.util.List;

import com.app.payloads.CartDTO;
import com.app.payloads.request.ItemRequest;
import com.app.payloads.response.AppResponse;

public interface CartService {

    AppResponse<CartDTO> addOrUpdateItem(final Long storeId, final ItemRequest request);

    List<CartDTO> getAllCarts();

    AppResponse<CartDTO> getCart(Long cartId);

    CartDTO updateCart(Long cartId, Long skuId, Integer quantity);

    void updateProductInCarts(Long cartId, Long skuId);

    AppResponse<String> deleteItem(Long cartId, Long cartItemId);

    AppResponse<List<CartDTO>> getAllCarts(final Long storeId);
}
