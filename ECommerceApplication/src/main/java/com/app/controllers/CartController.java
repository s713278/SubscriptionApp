package com.app.controllers;

import com.app.payloads.CartDTO;
import com.app.payloads.request.ItemRequest;
import com.app.payloads.response.ApiResponse;
import com.app.services.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "3. Shopping Cart Management API")
@RestController
@RequestMapping("/api/store")
@SecurityRequirement(name = "E-Commerce Application")
public class CartController {

  @Autowired private CartService cartService;

  // @PostMapping("/cart/{cartId}/sku/{skuId}/quantity/{quantity}")
  @PostMapping("/{store_id}/cart/{cart_id}/items")
  public ResponseEntity<ApiResponse<CartDTO>> addItemToCart(
      @PathVariable("store_id") Long storeId,
      @PathVariable("cart_id") Long cartId,
      @Valid @RequestBody ItemRequest request) {
    // TODO : pre Validation
    // TODO : Save the cart in database
    ApiResponse<CartDTO> cartDTO =
        cartService.addOrUpdateItem(storeId, cartId, request.getSkuId(), request.getQuantity());
    // TODO : post Validation
    return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
  }

  @PreAuthorize("ADMIN")
  @GetMapping("/{store_id}/admin/cart")
  public ResponseEntity<List<CartDTO>> getCarts() {
    List<CartDTO> cartDTOs = cartService.getAllCarts();
    return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.FOUND);
  }

  @GetMapping("/{store_id}/public/users/{emailId}/carts/{cartId}")
  public ResponseEntity<CartDTO> getCartById(
      @PathVariable String emailId, @PathVariable Long cartId) {
    CartDTO cartDTO = cartService.getCart(emailId, cartId);
    return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.FOUND);
  }

  /*@PutMapping("/{store_id}/public/carts/{cartId}/skus/{skuId}/quantity/{quantity}")
  @PutMapping("/{store_id}/carts/{cart_id}/items")
  public ResponseEntity<CartDTO> updateItem(
      @PathVariable Long cartId, @PathVariable Long skuId, @PathVariable Integer quantity) {
    CartDTO cartDTO = cartService.updateCart(cartId, skuId, quantity);
    
    CartDTO cartDTO =
            cartService.addOrUpdateItem(storeId, cartId, request.getSkuId(), request.getQuantity());
    
    return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
  }*/

  @DeleteMapping("/{store_id}/cart/{cart_id}/items/{cart_item_id}")
  public ResponseEntity<ApiResponse<String>> deleteProductFromCart(
      @PathVariable("store_id") Long storeId,
      @PathVariable("cart_id") Long cartId,
      @PathVariable("cart_item_id") Long cartItemId) {
    ApiResponse<String> status = cartService.delteItem(cartId, cartItemId);
    return new ResponseEntity<>(status, HttpStatus.OK);
  }
}
