package com.app.services.auth.dto;

import java.io.Serial;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthentication extends AbstractAuthenticationToken {
    
    /**
     * 
     */
    @Serial
    private static final long serialVersionUID = -9155810834003414934L;
    private final Long userId;

    public UserAuthentication(Long userId,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId=userId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

}
