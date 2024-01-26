package com.app.services;

import java.util.List;

import com.app.payloads.CartDTO;
import com.app.payloads.request.AddItemRequest;
import com.app.payloads.response.AddItemResponse;

public interface CartService {

	AddItemResponse addItemToCart(AddItemRequest request);

	CartDTO addSkuToCart(Long storeId, Long cartId, Long skuId, Integer quantity);

	List<CartDTO> getAllCarts();

	CartDTO getCart(String emailId, Long cartId);

	CartDTO updateCart(Long cartId, Long skuId, Integer quantity);

	void updateProductInCarts(Long cartId, Long skuId);

	String deleteProductFromCart(Long cartId, Long skuId);

}
