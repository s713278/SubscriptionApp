package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.CartDTO;
import com.app.payloads.request.AddItemRequest;
import com.app.payloads.response.AddItemResponse;
import com.app.services.CartService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/store/{store_id}")
@SecurityRequirement(name = "E-Commerce Application")
public class CartController {

	@Autowired
	private CartService cartService;

	// @PostMapping("/cart/{cartId}/sku/{skuId}/quantity/{quantity}")
	@PostMapping("/cart")
	public ResponseEntity<AddItemResponse> addItemToCart(@PathVariable Long storeId,
			@Valid @RequestBody AddItemRequest request) {
		// TODO : pre Validation
		// TODO : Save the cart in database
		CartDTO cartDTO = cartService.addSkuToCart(storeId, request.getCatId(), request.getSkuId(),
				request.getQuantity());
		// TODO : post Validation
		return new ResponseEntity<AddItemResponse>(new AddItemResponse(), HttpStatus.CREATED);
	}

	@PreAuthorize("ADMIN")
	@GetMapping("/admin/cart")
	public ResponseEntity<List<CartDTO>> getCarts() {
		List<CartDTO> cartDTOs = cartService.getAllCarts();
		return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.FOUND);
	}

	@GetMapping("/public/users/{emailId}/carts/{cartId}")
	public ResponseEntity<CartDTO> getCartById(@PathVariable String emailId, @PathVariable Long cartId) {
		CartDTO cartDTO = cartService.getCart(emailId, cartId);
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.FOUND);
	}

	@PutMapping("/public/carts/{cartId}/skus/{skuId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long cartId, @PathVariable Long skuId,
			@PathVariable Integer quantity) {
		CartDTO cartDTO = cartService.updateCart(cartId, skuId, quantity);
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
	}

	@DeleteMapping("/public/carts/{cartId}/product/{productId}")
	public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
		String status = cartService.deleteProductFromCart(cartId, productId);
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
}
