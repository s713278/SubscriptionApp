package com.app.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.config.UserInfoConfig;
import com.app.entites.Customer;
import com.app.repositories.CustomerRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user details for user is {}",username);
         Optional<Customer> user = null;
        try{
            Long userId=Long.parseLong(username);
             user = userRepo.findById(userId);
        }catch (Exception e) {
             user = userRepo.findByEmail(username.toLowerCase());
        }
        if (!user.isPresent()) {
            return null;
           // throw new ResourceNotFoundException("Customer", "email", username);
        }
        return user.map(UserInfoConfig::new).get();
    }
}
