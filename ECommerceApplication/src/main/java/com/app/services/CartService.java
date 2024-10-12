package com.app.services;

import com.app.payloads.CartDTO;
import com.app.payloads.request.ItemRequest;
import com.app.payloads.response.APIResponse;
import java.util.List;

public interface CartService {

    APIResponse<CartDTO> addOrUpdateItem(final Long storeId, final ItemRequest request);

    List<CartDTO> getAllCarts();

    APIResponse<CartDTO> getCart(Long cartId);

    CartDTO updateCart(Long cartId, Long skuId, Integer quantity);

    void updateProductInCarts(Long cartId, Long skuId);

    APIResponse<String> deleteItem(Long cartId, Long cartItemId);

    APIResponse<List<CartDTO>> getAllCarts(final Long storeId);
}
