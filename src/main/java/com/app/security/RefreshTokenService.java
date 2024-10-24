package com.app.security;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.app.auth.dto.AuthUserDetails;
import com.app.config.GlobalConfig;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    
      private final GlobalConfig globalConfig;

    // In-memory store for refresh tokens, can be stored in DB or cache
    private Map<String, String> refreshTokenStore = new HashMap<>();

    // Validate refresh token (e.g., check if it exists in the store or DB)
    public boolean validateRefreshToken(String token) {
        return refreshTokenStore.containsKey(token);
    }

    // Get username from the refresh token
    public String getUserIdFromRefreshToken(String token) {
        return refreshTokenStore.get(token);
    }

    // Store refresh token associated with a user
    public void storeRefreshToken(String username, String refreshToken) {
        refreshTokenStore.put(refreshToken, username);
    }

    
    // Generate and store a refresh token for the user
    public String createRefreshToken(AuthUserDetails authUserDetails) {
        String refreshToken = UUID.randomUUID().toString(); // Generate unique refresh token
        refreshTokenStore.put(refreshToken, String.valueOf(authUserDetails.getId()));
        return refreshToken;
    }
}
