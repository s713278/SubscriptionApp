package com.app.payloads.response;

import com.app.payloads.request.UpdateUserRequest;

import lombok.Data;

@Data
public class JWTAuthResponse {
    private String token;

    private UpdateUserRequest user;
}
