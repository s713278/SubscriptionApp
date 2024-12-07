package com.app.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.config.AppConstants;
import com.app.entites.CartItem;
import com.app.entites.Customer;
import com.app.entites.Role;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CartDTO;
import com.app.payloads.SkuDTO;
import com.app.payloads.UserDTO;
import com.app.payloads.request.OTPRequest;
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
    private final ModelMapper modelMapper;
    private final AddressValidator addressValidator;

    @Transactional(readOnly = true)
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Customer> pageUsers = repositoryManager.getCustomerRepo().findAll(pageDetails);

        List<Customer> users = pageUsers.getContent();

        if (users.isEmpty()) {
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
    public void updateUser(Long userId, UpdateUserRequest userDTO) {
        var user =fetchUserById(userId);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());
        repositoryManager.getCustomerRepo().save(user);
    }

    @Transactional
    public String deleteUser(Long userId) {
        Customer user = repositoryManager.getCustomerRepo().findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        List<CartItem> cartItems = user.getCart().getCartItems();
        Long cartId = user.getCart().getId();
        //Check if any active subscriptions
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
    public UserDTO fetchUserInfo(Long userId) {
        Customer customer = repositoryManager.getCustomerRepo().findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        // Creating the Data object
        return new UserDTO(customer.getId(), customer.getFirstName(), customer.getEmail(),
                customer.getFullMobileNumber(),customer.getDeliveryAddress());
    }

    @Transactional
    public void addNameAndAddress(Long userId, String name,Map<String, String> newDeliveryAddress) {
        fetchUserById(userId);
        addressValidator.validateAddress(userId, newDeliveryAddress);
        repositoryManager.getCustomerRepo().addNameAddress(userId, name,newDeliveryAddress);
    }

    @Transactional
    public void updateUserAddress(Long userId, Map<String, String> newDeliveryAddress) {
        fetchUserById(userId);
        addressValidator.validateAddress(userId, newDeliveryAddress);
        repositoryManager.getCustomerRepo().updateDeliveryAddress(userId, newDeliveryAddress);
    }


    public Customer fetchUserByMobileNumber(final Long mobile) {
        return repositoryManager.getCustomerRepo().findByMobile(mobile).orElseThrow(()->new APIException(APIErrorCode.API_404,"User is registered in system"))
;    }

    @Transactional
    public Customer createUser(Customer user){
        return repositoryManager.getCustomerRepo().save(user);
    }

    @Async
    @Transactional
    public void updateMobileVerifiedStatus(Long userId,boolean verified){
         repositoryManager.getCustomerRepo().updateMobileVerifiedStatus(userId,verified);
    }

    @Transactional(readOnly = true)
    public Customer fetchUserById(final Long userId) {
       return repositoryManager.getCustomerRepo().findById(userId)
                .orElseThrow(()->new  APIException(APIErrorCode.API_404,"User not existed in system."));
    }

    @Transactional
    public void updateDeliveryInstructions(Long userId, Map<String, String> newDeliveryAddress) {
        fetchUserById(userId);
      //  addressValidator.validateAddress(userId, newDeliveryAddress);
        repositoryManager.getCustomerRepo().updateDeliveryInstructions(userId, newDeliveryAddress);
    }

    public void createUserIfNotExisted(OTPRequest otpRequest){
        var existed = repositoryManager.getCustomerRepo().existsByMobile(otpRequest.getMobile());
        log.debug("User existed by mobile number ? {}",existed);
        if(!existed){
            Customer customer = new Customer();
            customer.setCountryCode(otpRequest.getCountryCode());
            customer.setMobile(otpRequest.getMobile());
            customer.setRegSource(otpRequest.getRegSource());
            customer.setPassword("9090"); // Use BCrypt for password encryption
            // Fetch the role and ensure it is managed
            Role role = repositoryManager.getRoleRepo().findById(AppConstants.USER_ROLE_ID)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found"));
            customer.getRoles().add(role);
            customer.setActive(true);
            customer.setMobileVerified(false);
            customer.setType(otpRequest.getUserType());
            customer.setOtpExpiration(LocalDateTime.now().plusMinutes(15)); // Set OTP expiration to 5 minutes
            customer = repositoryManager.getCustomerRepo().save(customer);
            log.info("User is created for mobile :{}",otpRequest.getMobile());
        }
    }
}
