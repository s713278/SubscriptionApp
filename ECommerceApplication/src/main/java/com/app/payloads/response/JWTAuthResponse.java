package com.app.payloads.response;

import com.app.payloads.UserDTO;

import lombok.Data;

@Data
public class JWTAuthResponse {
	private String token;

	private UserDTO user;
}