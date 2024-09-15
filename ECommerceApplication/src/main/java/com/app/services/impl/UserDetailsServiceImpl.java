package com.app.services.impl;

import com.app.config.UserInfoConfig;
import com.app.entites.Customer;
import com.app.repositories.CustomerRepo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> user = userRepo.findByEmail(username.toLowerCase());
        if (!user.isPresent()) {
            return null;
           // throw new ResourceNotFoundException("Customer", "email", username);
        }
        return user.map(UserInfoConfig::new).get();
    }
}
