package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Order;
import com.app.entites.OrderItem;
import com.app.entites.Sku;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CartDTO;
import com.app.payloads.SkuDTO;
import com.app.payloads.request.AddItemRequest;
import com.app.payloads.response.AddItemResponse;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.SkuRepo;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Transactional
@Service
public class CartServiceImpl implements CartService {

	private CartRepo cartRepo;

	private CartItemRepo cartItemRepo;

	private ModelMapper modelMapper;

	private SkuRepo skuRepo;

	@Override
	public CartDTO addSkuToCart(Long storeId, Long cartId, Long skuId, Integer quantity) {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		Sku sku = skuRepo.findById(skuId).orElseThrow(() -> new ResourceNotFoundException("Sku", "skuId", skuId));
		CartItem cartItem = cartItemRepo.findCartItemBySkuIdAndCartId(cartId, skuId);
		if (cartItem != null) {
			throw new APIException("Sku " + sku.getName() + " already exists in the cart");
		}
		if (sku.getQuantity() == 0) {
			throw new APIException(sku.getName() + " is not available");
		}
		if (sku.getQuantity() < quantity) {
			throw new APIException("Please, make an order of the " + sku.getName()
					+ " less than or equal to the quantity " + sku.getQuantity() + ".");
		}
		CartItem newCartItem = new CartItem();
		newCartItem.setSku(sku);
		newCartItem.setCart(cart);
		newCartItem.setQuantity(quantity);
		newCartItem.setDiscount(sku.getSalePrice() - sku.getListPrice());
		newCartItem.setPrice(sku.getSalePrice());
		cartItemRepo.save(newCartItem);
		cart.setTotalPrice(cart.getTotalPrice() + (sku.getSalePrice() * quantity));
		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		List<SkuDTO> skuDTOs = cart.getCartItems().stream().map(p -> modelMapper.map(p.getSku(), SkuDTO.class))
				.collect(Collectors.toList());
		cartDTO.setSkus(skuDTOs);
		return cartDTO;
	}

	@Override
	public List<CartDTO> getAllCarts() {
		List<Cart> carts = cartRepo.findAll();

		if (carts.size() == 0) {
			throw new APIException("No cart exists");
		}

		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

			List<SkuDTO> skus = cart.getCartItems().stream().map(p -> modelMapper.map(p.getSku(), SkuDTO.class))
					.collect(Collectors.toList());
			cartDTO.setSkus(skus);
			return cartDTO;
		}).collect(Collectors.toList());
		return cartDTOs;
	}

	@Override
	public CartDTO getCart(String emailId, Long cartId) {
		Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);

		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", cartId);
		}
		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		List<SkuDTO> skus = cart.getCartItems().stream().map(p -> modelMapper.map(p.getSku(), SkuDTO.class))
				.collect(Collectors.toList());
		cartDTO.setSkus(skus);
		return cartDTO;
	}

	@Override
	public String deleteProductFromCart(Long cartId, Long skuId) {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		CartItem cartItem = cartItemRepo.findCartItemBySkuIdAndCartId(cartId, skuId);

		if (cartItem == null) {
			throw new ResourceNotFoundException("Sku", "skuId", skuId);
		}

		cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getPrice() * cartItem.getQuantity()));

		Sku sku = cartItem.getSku();
		sku.setQuantity(sku.getQuantity() + cartItem.getQuantity());

		cartItemRepo.deleteCartItemBySkuIdAndCartId(cartId, skuId);

		return "Product " + cartItem.getSku().getName() + " removed from the cart !!!";
	}

	@Override
	public CartDTO updateCart(Long cartId, Long skuId, Integer quantity) {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		Sku sku = skuRepo.findById(skuId).orElseThrow(() -> new ResourceNotFoundException("Sku", "skuId", skuId));
		CartItem cartItem = cartItemRepo.findCartItemBySkuIdAndCartId(cartId, skuId);
		if (cartItem != null) {
			throw new APIException("Sku " + sku.getName() + " already exists in the cart");
		}
		if (sku.getQuantity() == 0) {
			throw new APIException(sku.getName() + " is not available");
		}
		if (sku.getQuantity() < quantity) {
			throw new APIException("Please, make an order of the " + sku.getName()
					+ " less than or equal to the quantity " + sku.getQuantity() + ".");
		}
		CartItem newCartItem = new CartItem();
		newCartItem.setSku(sku);
		newCartItem.setCart(cart);
		newCartItem.setQuantity(quantity);
		newCartItem.setDiscount(sku.getSalePrice() - sku.getListPrice());
		newCartItem.setPrice(sku.getSalePrice());
		cartItemRepo.save(newCartItem);
		cart.setTotalPrice(cart.getTotalPrice() + (sku.getSalePrice() * quantity));
		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		List<SkuDTO> skuDTOs = cart.getCartItems().stream().map(p -> modelMapper.map(p.getSku(), SkuDTO.class))
				.collect(Collectors.toList());
		cartDTO.setSkus(skuDTOs);
		return cartDTO;
	}

	@Override
	public void updateProductInCarts(Long cartId, Long productId) {
		// TODO Auto-generated method stub

	}

	@Override
	public AddItemResponse addItemToCart(AddItemRequest request) {

		Order order = new Order();
		order.setOrderStatus("NEW_ORDER");

		OrderItem orderItem = new OrderItem();
		orderItem.setQuantity(request.getQuantity());
		order.setItems(null);

		return new AddItemResponse();
	}

}
