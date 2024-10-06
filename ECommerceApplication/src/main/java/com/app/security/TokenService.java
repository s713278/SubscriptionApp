package com.app.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.app.config.GlobalConfig;
import com.app.entites.Customer;
import com.app.payloads.response.SignInResponse;
import com.app.repositories.CustomerRepo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TokenService {

    private static final String NSR_STORES = "NSR Stores";

    private final CustomerRepo customerRepo;
    
    private final GlobalConfig globalConfig;
    
   // private final static String secret=  globalConfig.getJwtConfig().getSecret();

    /**
     * Access Token
     * @param email
     * @return
     * @throws IllegalArgumentException
     * @throws JWTCreationException
     */
    @Transactional(readOnly = true)
    public String generateToken(String email) throws IllegalArgumentException, JWTCreationException {
        Customer user = customerRepo.findByEmail(email.toLowerCase()).get();
        String token = JWT.create()
                .withSubject(String.valueOf(user.getId())) // User ID
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now()
                        .plusSeconds(globalConfig.getJwtConfig().getAccessExpTime()))
               // .withClaim("email", user.getEmail())
                //.withClaim("cart_id", user.getCart().getId())
                .withClaim("roles",
                        user.getRoles().stream().map(role -> role.getRoleName())
                                .collect(Collectors.joining(email, "[", "]")))
                .withIssuer(NSR_STORES)
                .sign(Algorithm.HMAC256(globalConfig.getJwtConfig().getSecret()));
        return token;
    }
    
    @Transactional(readOnly = true)
    public String generateToken(Long id) throws IllegalArgumentException, JWTCreationException {
        Customer user = customerRepo.findById(id).get();
        String token = JWT.create()
                .withSubject(String.valueOf(user.getId())) // User ID
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now()
                        .plusSeconds(globalConfig.getJwtConfig().getRefreshExpTime()))
               //.withClaim("email", user.getEmail())
                //.withClaim("cart_id", user.getCart().getId())
                .withClaim("roles",
                        user.getRoles().stream().map(role -> role.getRoleName())
                                .collect(Collectors.joining(String.valueOf(id),"[", "]")))
                .withIssuer(NSR_STORES)
                .sign(Algorithm.HMAC256(globalConfig.getJwtConfig().getSecret()));
        SignInResponse loginResponse = SignInResponse.builder().userId(user.getId())
                .firstName(user.getFirstName()).lastName(user.getLastName()).userToken(token).build();
        return token;
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(globalConfig.getJwtConfig().getSecret()))
                // .withSubject("User Details")
                .withIssuer(NSR_STORES).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

    public UserClaims validateTokenAndRetrieveSubjectData(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(globalConfig.getJwtConfig().getSecret()))
                .withSubject("User Details").withIssuer(NSR_STORES)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return UserClaims.builder().userId(jwt.getClaim("userId").asString())
                .storeId(jwt.getClaim("storeId").asString()).build();
    }
    
    // Validate token
    public boolean validateToken(String token) {
        try {
            DecodedJWT jwt = JWT
                    .require(Algorithm.HMAC256(globalConfig.getJwtConfig().getSecret()))
                    .withIssuer(NSR_STORES)
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Extract username from the token
    public String getUsernameFromToken(String token) {
         try {
             DecodedJWT jwt = JWT
                     .require(Algorithm.HMAC256(globalConfig.getJwtConfig().getSecret()))
                     .withIssuer(NSR_STORES)
                     .build()
                     .verify(token);
             return jwt.getSubject();
         } catch (Exception e) {
             return null;
         }
    }
}
