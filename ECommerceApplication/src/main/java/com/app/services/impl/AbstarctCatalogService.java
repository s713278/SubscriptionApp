package com.app.services.impl;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Sku;
import com.app.entites.Store;
import com.app.entites.User;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.OrderItemRepo;
import com.app.repositories.OrderRepo;
import com.app.repositories.PaymentRepo;
import com.app.repositories.StoreRepo;
import com.app.repositories.UserRepo;
import com.app.services.CartService;
import com.app.services.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Slf4j
public abstract class AbstarctCatalogService {

    protected UserRepo userRepo;

    protected CartRepo cartRepo;

    protected OrderRepo orderRepo;

    public PaymentRepo paymentRepo;

    public OrderItemRepo orderItemRepo;

    public CartItemRepo cartItemRepo;

    public UserService userService;

    public CartService cartService;

    public StoreRepo storeRepo;

    public ModelMapper modelMapper;

    protected User validateUser(Long userId) {
        log.debug("Validate User {} ", userId);
        User user =
                userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        return user;
    }

    protected Cart validateCart(Long cartId, User user) {
        log.debug("Validate Cart {} for User {} ", cartId, user.getId());
        Cart cart = user.getCart();
        if (cart.getId().compareTo(cartId) != 0) {
            throw new APIException(String.format(
                    "Malformed request while placing the order where cartId: %d is not belongs to userId : %d",
                    cartId, user.getId()));
        }
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.size() == 0) {
            throw new APIException("Cart is empty");
        }
        return cart;
    }

    protected Store validateCartItemsAndStore(Long storeId, Cart cart) {
        log.debug("Validate CartItems {} against the store {}  ", cart.getId(), storeId);
        Store store = storeRepo
                .findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", storeId));
        boolean allItemsStoreMatched =
                cart.getCartItems().stream().allMatch(t -> t.getSku().getStore().getId() == storeId);

        if (!allItemsStoreMatched) {
            throw new APIException(
                    String.format("Not all order items have store id % in the cart % ", storeId, cart.getId()));
        }
        return store;
    }

    protected void updateCartAndSkuQuantities(Cart cart) {
        cart.getCartItems().forEach(cartItem -> {
            int quantity = cartItem.getQuantity();
            Sku sku = cartItem.getSku();
            cartService.deleteItem(cart.getId(), cartItem.getCartItemId());
            sku.setQuantity(sku.getQuantity() - quantity);
        });

        cart.setTotalPrice(0D);
    }
}
