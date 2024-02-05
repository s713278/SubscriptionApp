package com.app.services.impl;

import com.app.config.UserInfoConfig;
import com.app.entites.User;
import com.app.exceptions.ResourceNotFoundException;
import com.app.repositories.UserRepo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmailIgnoreCase(username);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "email", username);
        }
        return user.map(UserInfoConfig::new).get();
    }
}
