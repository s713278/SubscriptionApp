package com.app.services.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.app.auth.dto.AuthUserDetails;
import com.app.entites.Customer;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.repositories.RepositoryManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RepositoryManager repositoryManager;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("Load user details for user is {}",username);
         Optional<Customer> user = null;
        try{
            Long mobile=Long.parseLong(username);
            user = repositoryManager.getCustomerRepo().findByMobile(mobile);
        }catch (Exception e) {
             throw new APIException(APIErrorCode.API_419,e.getMessage());
        }
        if (user.isEmpty()) {
             throw new APIException(APIErrorCode.API_401,"Incorrect Login Details!");
        }
        return user.map(AuthUserDetails::new).get();
    }
   
}
