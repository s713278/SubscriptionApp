package com.app.services.impl;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Customer;
import com.app.entites.Sku;
import com.app.entites.Vendor;
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
import org.springframework.util.Assert;

@Slf4j
public abstract class AbstarctCatalogService {

    protected UserRepo userRepo;

    protected CartRepo cartRepo;

    protected OrderRepo orderRepo;

    protected PaymentRepo paymentRepo;

    protected OrderItemRepo orderItemRepo;

    protected CartItemRepo cartItemRepo;

    protected UserService userService;

    protected CartService cartService;

    protected StoreRepo storeRepo;

    public ModelMapper modelMapper;
    
    

    public AbstarctCatalogService(UserRepo userRepo, CartRepo cartRepo, OrderRepo orderRepo, PaymentRepo paymentRepo,
            OrderItemRepo orderItemRepo, CartItemRepo cartItemRepo, UserService userService, CartService cartService,
            StoreRepo storeRepo, ModelMapper modelMapper) {
        super();
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
        this.orderItemRepo = orderItemRepo;
        this.cartItemRepo = cartItemRepo;
        this.userService = userService;
        this.cartService = cartService;
        this.storeRepo = storeRepo;
        this.modelMapper = modelMapper;
    }

    protected Customer validateUser(Long userId) {
        log.debug("Validate User {} ", userId);
        Customer user =
                userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        return user;
    }

    protected Cart validateCart(Long cartId, Customer user) {
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

    protected Vendor validateCartItemsAndStore(Long storeId, Cart cart) {
        log.debug("Validate CartItems {} against the store {}  ", cart.getId(), storeId);
        Vendor store = storeRepo
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

    /**
     * <p>Updating the sku's inventory.</p<
     * <p>Clean up the user's cart.</p<
     * @param cart
     */
    protected void updateCartAndSkuQuantities(Cart cart) {
        cart.getCartItems().forEach(cartItem -> {
             int quantity = cartItem.getQuantity();
             Sku sku = cartItem.getSku();
             sku.setQuantity(sku.getQuantity() - quantity);
        });
        cart.getCartItems().clear();
        cart.setTotalPrice(0D);
        cartItemRepo.deleteByCartId(cart.getId().longValue());
        Assert.isTrue(cart.getTotalPrice().compareTo(0D)==0,String.format("Cart %d total amount is not zero but expection is ZERO.",cart.getId()));
        Assert.isTrue(cart.getCartItems().isEmpty(), String.format("Cart %d have %d items and expectation is EMPTY",cart.getId(),cart.getCartItems().size()));
    }
}
