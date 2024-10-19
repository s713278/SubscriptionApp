package com.app.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entites.CartItem;
import com.app.entites.Customer;
import com.app.entites.type.AddressTypeConverter;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CartDTO;
import com.app.payloads.SkuDTO;
import com.app.payloads.UserDTO;
import com.app.payloads.request.UpdateUserRequest;
import com.app.payloads.response.UserResponse;
import com.app.repositories.RepositoryManager;
import com.app.services.validator.AddressValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final RepositoryManager repositoryManager;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final AddressTypeConverter addressTypeConverter;

    private final AddressValidator addressValidator;

    @Transactional(readOnly = true)
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Customer> pageUsers = repositoryManager.getCustomerRepo().findAll(pageDetails);

        List<Customer> users = pageUsers.getContent();

        if (users.size() == 0) {
            throw new APIException(APIErrorCode.API_400, "No User exists !!!");
        }

        List<UpdateUserRequest> userDTOs = users.stream().map(user -> {
            UpdateUserRequest dto = modelMapper.map(user, UpdateUserRequest.class);
            /*
             * if (user.getAddresses().size() != 0) { dto.setAddress(modelMapper.map(
             * user.getAddresses().stream().findFirst().get(), AddressDTO.class)); }
             */
            CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);
            List<SkuDTO> skuDTOs = user.getCart().getCartItems().stream()
                    .map(item -> modelMapper.map(item.getSku(), SkuDTO.class)).collect(Collectors.toList());
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

    @Transactional(readOnly = true)
    public UpdateUserRequest getUserById(final Long userId) {
        Customer user = repositoryManager.getCustomerRepo().findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        UpdateUserRequest userDTO = modelMapper.map(user, UpdateUserRequest.class);
        /*
         * userDTO.setAddress(
         * modelMapper.map(user.getAddresses().stream().findFirst().get(),
         * AddressDTO.class));
         */
        if (user.getCart() != null) {
            CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);
            List<SkuDTO> skuDTOs = user.getCart().getCartItems().stream()
                    .map(item -> modelMapper.map(item.getSku(), SkuDTO.class)).collect(Collectors.toList());
            // userDTO.getCart().setSkus(skuDTOs);
        }
        return userDTO;
    }

    @Transactional
    public UpdateUserRequest updateUser(Long userId, UpdateUserRequest userDTO) {
        Customer user = repositoryManager.getCustomerRepo().findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        userDTO = modelMapper.map(user, UpdateUserRequest.class);
        return userDTO;
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public UserDTO getUserInfo(Long userId) {
        Customer customer = repositoryManager.getCustomerRepo().findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        // Creating the Data object
        UserDTO data = new UserDTO(customer.getId(), customer.getFirstName(), customer.getEmail(),
                customer.getMobile());
        // Creating the GetUserResponse object
        return data;
    }

    @Transactional
    public void updateUserAddress(Long userId, Map<String, String> newDeliveryAddress) {
        repositoryManager.getCustomerRepo().findById(userId)
                .orElseThrow(() -> new APIException(APIErrorCode.API_404, "User not Found!!"));
        addressValidator.validateAddress(userId, newDeliveryAddress);
        repositoryManager.getCustomerRepo().updateDeliveryAddress(userId, newDeliveryAddress);
    }

}
