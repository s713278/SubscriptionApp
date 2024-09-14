package com.app.security;

import com.app.entites.Customer;
import com.app.payloads.response.SignInResponse;
import com.app.repositories.CustomerRepo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JWTUtil {

    private static final String NSR_STORES = "NSR Stores";

    @Autowired
    private CustomerRepo customerRepo;

    @Value("${jwt_secret}")
    private String secret;

    @Transactional(readOnly = true)
    public SignInResponse generateToken(String email) throws IllegalArgumentException, JWTCreationException {
        Customer user = customerRepo.findByEmail(email.toLowerCase()).get();
        String token = JWT.create().withSubject(String.valueOf(user.getId())) // User ID
                .withIssuedAt(Instant.now()).withExpiresAt(Instant.now().plusSeconds(60 * 60))
                .withClaim("email", user.getEmail())
                //.withClaim("cart_id", user.getCart().getId())
                .withClaim("roles",
                        user.getRoles().stream().map(role -> role.getRoleName())
                                .collect(Collectors.joining(email, "[", "]")))
                .withIssuer(NSR_STORES).sign(Algorithm.HMAC256(secret));
        SignInResponse loginResponse = SignInResponse.builder().userId(String.valueOf(user.getId()))
                .firstName(user.getFirstName()).lastName(user.getLastName()).userToken(token).build();
        return loginResponse;
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                // .withSubject("User Details")
                .withIssuer(NSR_STORES).build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }

    public UserClaims validateTokenAndRetrieveSubjectData(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).withSubject("User Details").withIssuer(NSR_STORES)
                .build();

        DecodedJWT jwt = verifier.verify(token);

        return UserClaims.builder().userId(jwt.getClaim("userId").asString())
                .storeId(jwt.getClaim("storeId").asString()).build();
    }
}
