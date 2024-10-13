package com.app.controllers;

import com.app.payloads.CartDTO;
import com.app.payloads.request.ItemRequest;
import com.app.payloads.response.APIResponse;
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

@Tag(name = "6. Cart Management")
@RestController
@RequestMapping("/store/{store_id}")
@SecurityRequirement(name = "E-Commerce Application")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/cart/items")
    public ResponseEntity<APIResponse<CartDTO>> addorUpdateItemToCart(@PathVariable("store_id") Long storeId,
            @Valid @RequestBody ItemRequest request) {
        // TODO : Pre Validation
        // TODO : Save the cart in database
        APIResponse<CartDTO> cartDTO = cartService.addOrUpdateItem(storeId, request);
        // TODO : post Validation
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("STORE")
    @GetMapping("/cart/items")
    public ResponseEntity<List<CartDTO>> getCarts(@PathVariable("store_id") Long storeId) {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.FOUND);
    }

    @GetMapping("/cart/{cart_id}/items")
    public ResponseEntity<APIResponse<CartDTO>> getCartById(@PathVariable("store_id") Long storeId,
            @PathVariable("cart_id") Long cartId) {
        APIResponse<CartDTO> cartDTO = cartService.getCart(cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    /*
     * @PutMapping(
     * "/{store_id}/public/carts/{cartId}/skus/{skuId}/quantity/{quantity}")
     *
     * @PutMapping("/{store_id}/carts/{cart_id}/items") public
     * ResponseEntity<CartDTO> updateItem(
     *
     * @PathVariable Long cartId, @PathVariable Long skuId, @PathVariable Integer
     * quantity) { CartDTO cartDTO = cartService.updateCart(cartId, skuId,
     * quantity);
     *
     * CartDTO cartDTO = cartService.addOrUpdateItem(storeId, cartId,
     * request.getSkuId(), request.getQuantity());
     *
     * return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK); }
     */

    @PreAuthorize("USER")
    @DeleteMapping("/cart/{cart_id}/items/{cart_item_id}")
    public ResponseEntity<APIResponse<String>> deleteProductFromCart(@PathVariable("store_id") Long storeId,
            @PathVariable("cart_id") Long cartId, @PathVariable("cart_item_id") Long cartItemId) {
        APIResponse<String> status = cartService.deleteItem(cartId, cartItemId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
