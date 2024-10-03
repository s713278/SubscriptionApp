package com.app.services;

import com.app.payloads.CustomerDTO;
import com.app.payloads.response.GetUserResponse;
import com.app.payloads.response.UserResponse;

public interface UserService {
    CustomerDTO registerUser(CustomerDTO userDTO);

    UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CustomerDTO getUserById(Long userId);

    CustomerDTO updateUser(Long userId, CustomerDTO userDTO);

    String deleteUser(Long userId);
    
    GetUserResponse getUserInfo(Long userId);
}
