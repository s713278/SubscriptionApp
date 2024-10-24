package com.app.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.auth.dto.UserAuthentication;
import com.app.entites.Customer;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.repositories.RepositoryManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiAuthValidator {

    private final RepositoryManager repositoryManager;
    
    @Transactional(readOnly = false)
    public Optional<UserAuthentication> authenticateUser(Long userId) {
        log.debug("Load user details by user id :{}",userId);
         Optional<Customer> user = null;
        try{
            user = repositoryManager.getCustomerRepo().findById(userId);
        }catch (Exception e) {
             throw new APIException(APIErrorCode.API_401,e.getMessage());
        }
        if (user.isEmpty()) {
             throw new APIException(APIErrorCode.API_401,"User not existed in system.");
        }
       
        List<GrantedAuthority> authorities = user.get().getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
        return Optional.of(new UserAuthentication(user.get().getId(), authorities));
    }
}
