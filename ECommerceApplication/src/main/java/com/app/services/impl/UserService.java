package com.app.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.config.AppConstants;
import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Customer;
import com.app.entites.Role;
import com.app.entites.type.AddressTypeConverter;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CartDTO;
import com.app.payloads.CustomerDTO;
import com.app.payloads.SkuDTO;
import com.app.payloads.response.GetUserResponse;
import com.app.payloads.response.UserResponse;
import com.app.repositories.RepositoryManager;
import com.app.services.validator.AddressValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

	private final RepositoryManager repositoryManager;

	private final PasswordEncoder passwordEncoder;

	private final ModelMapper modelMapper;

	private final AddressTypeConverter addressTypeConverter;

	private final AddressValidator addressValidator;

	public CustomerDTO registerUser(CustomerDTO customerReq) {

		try {
			String encodedPass = passwordEncoder.encode(customerReq.getPassword());
			customerReq.setPassword(encodedPass);
			Customer user = modelMapper.map(customerReq, Customer.class);

			Cart cart = new Cart();
			user.setCart(cart);

			Role role = repositoryManager.getRoleRepo().findById(AppConstants.USER_ROLE_ID).get();
			user.getRoles().add(role);

			log.debug("(User delivery address \t {}", customerReq.getDeliveryAddress());
			if (customerReq.getDeliveryAddress() != null) {
				/*
				 * String country = userDTO.getAddress().getCountry(); String state =
				 * userDTO.getAddress().getState(); String city =
				 * userDTO.getAddress().getCity(); String pincode =
				 * userDTO.getAddress().getPincode(); String street =
				 * userDTO.getAddress().getAddress1(); String buildingName =
				 * userDTO.getAddress().getAddress2();
				 * 
				 * Address address =
				 * addressRepo.findByCountryAndStateAndCityAndPincodeAndAddress1AndAddress2(
				 * country, state, city, pincode, street, buildingName);
				 * 
				 * if (address == null) { address = new Address(country, state, city, pincode,
				 * street, buildingName);
				 * 
				 * address = addressRepo.save(address); }
				 */
				user.setDeliveryAddress(addressTypeConverter.toEntityType(customerReq.getDeliveryAddress()));
				// user.setAddresses(List.of(address));
			}
			cart.setUser(user);
			Customer registeredUser = repositoryManager.getCustomerRepo().saveAndFlush(user);
			customerReq = modelMapper.map(registeredUser, CustomerDTO.class);
			// userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(),
			// AddressDTO.class));
			return customerReq;
		} catch (DataIntegrityViolationException e) {
			log.error("Error occured while creating user for user {}", customerReq.getEmail(), e);
			throw new APIException(APIErrorCode.API_417, e.getMessage() + customerReq.getEmail());
		}
	}

	public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Customer> pageUsers = repositoryManager.getCustomerRepo().findAll(pageDetails);

		List<Customer> users = pageUsers.getContent();

		if (users.size() == 0) {
			throw new APIException(APIErrorCode.API_400, "No User exists !!!");
		}

		List<CustomerDTO> userDTOs = users.stream().map(user -> {
			CustomerDTO dto = modelMapper.map(user, CustomerDTO.class);
			/*
			 * if (user.getAddresses().size() != 0) { dto.setAddress(modelMapper.map(
			 * user.getAddresses().stream().findFirst().get(), AddressDTO.class)); }
			 */
			CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);
			List<SkuDTO> skuDTOs = user.getCart().getCartItems().stream()
					.map(item -> modelMapper.map(item.getSku(), SkuDTO.class)).collect(Collectors.toList());
			dto.setCart(cart);
			// dto.getCart().setSkus(skuDTOs);
			return dto;
		}).collect(Collectors.toList());

		UserResponse userResponse = new UserResponse();
		userResponse.setContent(userDTOs);
		userResponse.setPageNumber(pageUsers.getNumber());
		userResponse.setPageSize(pageUsers.getSize());
		userResponse.setTotalElements(pageUsers.getTotalElements());
		userResponse.setTotalPages(pageUsers.getTotalPages());
		userResponse.setLastPage(pageUsers.isLast());
		return userResponse;
	}

	public CustomerDTO getUserById(final Long userId) {
		Customer user = repositoryManager.getCustomerRepo().findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
		CustomerDTO userDTO = modelMapper.map(user, CustomerDTO.class);
		/*
		 * userDTO.setAddress(
		 * modelMapper.map(user.getAddresses().stream().findFirst().get(),
		 * AddressDTO.class));
		 */
		if (user.getCart() != null) {
			CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);
			List<SkuDTO> skuDTOs = user.getCart().getCartItems().stream()
					.map(item -> modelMapper.map(item.getSku(), SkuDTO.class)).collect(Collectors.toList());
			userDTO.setCart(cart);

			// userDTO.getCart().setSkus(skuDTOs);
		}
		return userDTO;
	}

	public CustomerDTO updateUser(Long userId, CustomerDTO userDTO) {
		Customer user = repositoryManager.getCustomerRepo().findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
		String encodedPass = passwordEncoder.encode(userDTO.getPassword());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setMobile(userDTO.getMobile());
		user.setEmail(userDTO.getEmail());
		user.setPassword(encodedPass);
		/*
		 * if (userDTO.getAddress() != null) { String country =
		 * userDTO.getAddress().getCountry(); String state =
		 * userDTO.getAddress().getState(); String city =
		 * userDTO.getAddress().getCity(); String pincode =
		 * userDTO.getAddress().getPincode(); String street =
		 * userDTO.getAddress().getAddress1(); String buildingName =
		 * userDTO.getAddress().getAddress2();
		 * 
		 * Address address =
		 * addressRepo.findByCountryAndStateAndCityAndPincodeAndAddress1AndAddress2(
		 * country, state, city, pincode, street, buildingName);
		 * 
		 * if (address == null) { address = new Address(street, buildingName, city,
		 * state, country, pincode); address = addressRepo.save(address);
		 * user.setAddresses(List.of(address)); } }
		 */
		userDTO = modelMapper.map(user, CustomerDTO.class);
		/*
		 * userDTO.setAddress(
		 * modelMapper.map(user.getAddresses().stream().findFirst().get(),
		 * AddressDTO.class));
		 */
		CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);
		List<SkuDTO> skuDTOs = user.getCart().getCartItems().stream()
				.map(item -> modelMapper.map(item.getSku(), SkuDTO.class)).collect(Collectors.toList());
		userDTO.setCart(cart);
		// userDTO.getCart().setSkus(skuDTOs);
		return userDTO;
	}

	public String deleteUser(Long userId) {
		Customer user = repositoryManager.getCustomerRepo().findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		List<CartItem> cartItems = user.getCart().getCartItems();
		Long cartId = user.getCart().getId();

		/*
		 * cartItems.forEach(item -> {
		 *
		 * Long skuId = item.getSku().getSkuId();
		 *
		 * cartService.deleteProductFromCart(cartId, skuId); });
		 */

		repositoryManager.getCustomerRepo().delete(user);

		return "User with userId " + userId + " deleted successfully!!!";
	}

	public GetUserResponse getUserInfo(Long userId) {
		Customer customer = repositoryManager.getCustomerRepo().findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
		// Creating the Data object
		GetUserResponse.Data data = new GetUserResponse.Data(customer.getId(), customer.getFirstName(),
				customer.getEmail(), customer.getMobile());
		// Creating the GetUserResponse object
		GetUserResponse response = new GetUserResponse(true, data);
		return response;
	}

	@Transactional
	public void updateUserAddress(Long userId, Map<String, String> newDeliveryAddress) {
		try {
			repositoryManager.getCustomerRepo().findById(userId).orElseThrow(()->new APIException(APIErrorCode.API_404, "User not Found!!"));
			addressValidator.validateAddress(userId, newDeliveryAddress);
			repositoryManager.getCustomerRepo().updateDeliveryAddress(userId, newDeliveryAddress);
		} catch (Exception e) {
			throw new APIException(APIErrorCode.API_421, e.getMessage());
		}
	}

}
