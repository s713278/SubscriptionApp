package com.app.auth.dto;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthentication extends AbstractAuthenticationToken {
    
    /**
     * 
     */
    private static final long serialVersionUID = -9155810834003414934L;
    private Long userId;

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
