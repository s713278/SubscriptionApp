package com.app.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.app.config.GlobalConfig;
import com.app.entites.Customer;
import com.app.entites.Role;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.repositories.RepositoryManager;
import com.app.services.auth.dto.AuthUserDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class TokenService {

    private static final String NSR_STORES = "SubNRenewals";
    
    private final GlobalConfig globalConfig;
    
    private final RepositoryManager repositoryManager;

    public String generateToken(AuthUserDetails authUserDetails) {
        try{
            String token = JWT.create()
                    .withSubject(String.valueOf(authUserDetails.getId())) // User ID
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now()
                            .plusSeconds(globalConfig.getJwtConfig().getRefreshExpTime()))
                    .withClaim("roles",
                            authUserDetails.getAuthorities().stream().map(auth -> auth.getAuthority())
                                    .collect(Collectors.joining(String.valueOf(authUserDetails.getId()),"[", "]")))
                    .withIssuer(NSR_STORES)
                    .sign(Algorithm.HMAC256(globalConfig.getJwtConfig().getSecret()));
            return token;
        }catch (Exception e){
            log.error("Unable to generate access token for user: {}",authUserDetails.getId(),e);
            throw new APIException(APIErrorCode.API_401,String.format("Unable to generate access token for user :%s",authUserDetails.getId()));
        }

    }
    
    /**
     * <p>
     * @param id
     * @return
     * @throws IllegalArgumentException
     * @throws JWTCreationException
     */
    @Transactional(readOnly = true)
    public String generateToken(String id) throws IllegalArgumentException, JWTCreationException {
        Customer user = repositoryManager.getCustomerRepo().findById(Long.parseLong(id)).orElseThrow(() -> new APIException(APIErrorCode.API_401, "No user existed in system"));
        String token = JWT.create()
                .withSubject(String.valueOf(user.getId())) // User ID
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now()
                        .plusSeconds(globalConfig.getJwtConfig().getRefreshExpTime()))
               //.withClaim("email", user.getEmail())
                //.withClaim("cart_id", user.getCart().getId())
                .withClaim("roles",
                        user.getRoles().stream().map(Role::getRoleName)
                                .collect(Collectors.joining(id,"[", "]")))
                .withIssuer(NSR_STORES)
                .sign(Algorithm.HMAC256(globalConfig.getJwtConfig().getSecret()));
        return token;
    }

    /**
     * <p>
     * Return userId from the token.
     * </p>
     * @param token
     * @return
     * @throws JWTVerificationException
     */
    public String validateTokenAndRetrieveSubject(String token)  {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(globalConfig.getJwtConfig().getSecret()))
                    // .withSubject("User Details")
                    .withIssuer(NSR_STORES).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (IllegalArgumentException e) {
        throw new APIException(APIErrorCode.API_400	, e.getMessage());
        } catch (JWTVerificationException e) {
            throw new APIException(APIErrorCode.API_401	, e.getMessage());
        }
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
            throw new APIException(APIErrorCode.API_401	, e.getMessage());
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
