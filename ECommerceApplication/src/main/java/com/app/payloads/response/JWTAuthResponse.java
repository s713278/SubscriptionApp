package com.app.payloads.response;

import com.app.payloads.CustomerDTO;

import lombok.Data;

@Data
public class JWTAuthResponse {
    private String token;

    private CustomerDTO user;
}
