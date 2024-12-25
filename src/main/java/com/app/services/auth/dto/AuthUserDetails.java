package com.app.services.auth.dto;

import java.io.Serial;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.entites.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class AuthUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private Long id;
    private String email;
    private Long mobile;
    private String password;
    private boolean isMobileVerified;
    private boolean isEmailVerified;
    private Set<GrantedAuthority> authorities;
    private Map<String,String> address;
    private String fullMobileNumber;

    public AuthUserDetails(Customer user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.mobile= user.getMobile();
        this.id=user.getId();
        isMobileVerified = !Objects.isNull(user.getMobileVerified()) && user.getMobileVerified();
        isEmailVerified = !Objects.isNull(user.getEmail()) && user.getEmailVerified();
        log.info("User mobile {} and role {}", user.getMobile(), user.getRoles());
        this.authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
        this.fullMobileNumber=user.getFullMobileNumber();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return String.valueOf(mobile);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
