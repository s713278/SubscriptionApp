package com.app.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Sku;
import com.app.entites.Vendor;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CartDTO;
import com.app.payloads.CartItemDTO;
import com.app.payloads.SkuDTO;
import com.app.payloads.request.ItemRequest;
import com.app.payloads.response.APIResponse;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.CustomerRepo;
import com.app.repositories.SkuRepo;
import com.app.repositories.VendorRepo;
import com.app.services.CartService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Transactional
@Service
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;

    private final CartItemRepo cartItemRepo;

    private final ModelMapper modelMapper;

    private final SkuRepo skuRepo;

    private final VendorRepo storeRepo;

    private final CustomerRepo userRepo;

    @Override
    public APIResponse<CartDTO> addOrUpdateItem(final Long storeId, final ItemRequest request) {
        Long userId = request.getUserId();
        Long skuId = request.getSkuId();
        Integer quantity = request.getQuantity();

        Long cartId = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId)).getCart().getId();

        Vendor store = storeRepo.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", storeId));

        Sku sku = skuRepo.findByIdAndStoreId(skuId)
                .orElseThrow(() -> new ResourceNotFoundException("Sku", "skuId", skuId));
        
        

      /*  if (sku.getStore().getId() != storeId && sku.getId() == skuId) {
            throw new APIException(APIErrorCode.API_400,
                    "The item " + sku.getName() + "is not avaiable @ store " + store.getBusinessName() + ".");
        }
        if (sku.getQuantity() == 0 && sku.getStore().getId() == storeId) {
            throw new APIException(APIErrorCode.API_400,
                    sku.getName() + " is not out of stack at " + sku.getStore().getBusinessName());
        }*/

        if (quantity > sku.getStock() ) {
            throw new APIException(APIErrorCode.API_400,"Please, make an order of the " + sku.getName()
                    + " less than or equal to the quantity " + sku.getStock() + ".");
        }

        // Retrieve Existing Cart if any
        Cart shoppingCart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem existingItem = cartItemRepo.findCartItemBySkuIdAndCartIdAndSkuId(skuId, cartId, storeId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
           //TODO: existingItem.setAmount(existingItem.getQuantity() * sku.getSalePrice());
            // Reducing the quantity
            // sku.setQuantity(sku.getQuantity() - quantity);
        } else {
            existingItem = new CartItem();
            existingItem.setSku(sku);
            existingItem.setCart(shoppingCart);
            existingItem.setQuantity(quantity);
            //TODO:   existingItem.setAmount(quantity * sku.getSalePrice());
        }
        //TODO: existingItem.setUnitPrice(sku.getSalePrice());
        //TODO:  existingItem.setDiscount(existingItem.getQuantity() * (sku.getListPrice() - sku.getSalePrice()));
        cartItemRepo.saveAndFlush(existingItem);

        Double totalAmount = shoppingCart.getCartItems().stream()
                .map(cartItem1 -> cartItem1.getQuantity() * cartItem1.getUnitPrice()).mapToDouble(Double::doubleValue)
                .sum();
        shoppingCart.setTotalPrice(BigDecimal.valueOf(totalAmount));
        // shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() +
        // (cartItem.getUnitPrice() *
        // quantity));

        // New CartItemDTO
        CartItemDTO cartItemDTO = modelMapper.map(existingItem, CartItemDTO.class);
        CartDTO cartDTO = modelMapper.map(shoppingCart, CartDTO.class);
        List<CartItemDTO> cartItemDTOs = shoppingCart.getCartItems().stream()
                .map(cartItemEntity -> modelMapper.map(cartItemEntity, CartItemDTO.class)).collect(Collectors.toList());
        cartItemDTOs.add(cartItemDTO);
        // cartDTO.setItems(cartItemDTOs);
        cartRepo.saveAndFlush(shoppingCart);
        return APIResponse.success(HttpStatus.OK.value(), cartDTO);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepo.findAll();

        if (carts.size() == 0) {
            throw new APIException(APIErrorCode.API_400,"No cart exists");
        }

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<SkuDTO> skus = cart.getCartItems().stream().map(p -> modelMapper.map(p.getSku(), SkuDTO.class))
                    .collect(Collectors.toList());
            // cartDTO.setSkus(skus);
            return cartDTO;
        }).collect(Collectors.toList());
        return cartDTOs;
    }

    @Override
    public APIResponse<CartDTO> getCart(Long cartId) {
        // Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        return APIResponse.success(HttpStatus.OK.value(), modelMapper.map(cart, CartDTO.class));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse<String> deleteItem(Long cartId, Long cartItemId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem matchedCartITem = cart.getCartItems().stream().filter(t -> t.getId() == cartItemId).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "cartItemId", cartItemId));
        cartItemRepo.deleteBycartItemIdAndCartId(cartItemId, cartId);
        if (cart.getCartItems().isEmpty()) {
            cart.setTotalPrice(BigDecimal.valueOf(0));
        } else {
            Double totalAmount = cart.getCartItems().stream()
                    .map(cartItem1 -> cartItem1.getQuantity() * cartItem1.getUnitPrice())
                    .mapToDouble(Double::doubleValue).sum();
            cart.setTotalPrice(BigDecimal.valueOf(totalAmount));
        }
        cartItemRepo.flush();
        return APIResponse.success(HttpStatus.OK.value(),
                "Item " + matchedCartITem.getSku().getName() + " removed from the cart !!!");
    }

    @Override
    public CartDTO updateCart(Long cartId, Long skuId, Integer quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Sku sku = skuRepo.findById(skuId).orElseThrow(() -> new ResourceNotFoundException("Sku", "skuId", skuId));
        CartItem cartItem = cartItemRepo.findCartItemBySkuIdAndCartIdAndSkuId(skuId, 1L, 1L);

        if (cartItem != null) {
            throw new APIException(APIErrorCode.API_400,"Sku " + sku.getName() + " already exists in the cart");
        }
        if (sku.getStock() == 0) {
            throw new APIException(APIErrorCode.API_400, sku.getName() + " is out of stock and not available");
        }
        if (sku.getStock() < quantity) {
            throw new APIException(APIErrorCode.API_400,"Please, make an order of the " + sku.getName()
                    + " less than or equal to the quantity " + sku.getStock() + ".");
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setSku(sku);
        // newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        //TODO:  newCartItem.setDiscount(sku.getSalePrice() - sku.getListPrice());
        //TODO:  newCartItem.setUnitPrice(sku.getSalePrice());
        cartItemRepo.save(newCartItem);
        //TODO:  cart.setTotalPrice(cart.getTotalPrice() + (sku.getSalePrice() * quantity));
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<SkuDTO> skuDTOs = cart.getCartItems().stream().map(p -> modelMapper.map(p.getSku(), SkuDTO.class))
                .collect(Collectors.toList());
        // cartDTO.setSkus(skuDTOs);
        return cartDTO;
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        // TODO Auto-generated method stub

    }

    @Override
    public APIResponse<List<CartDTO>> getAllCarts(Long storeId) {
        return null;
    }
}
