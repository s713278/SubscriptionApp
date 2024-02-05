package com.app.controllers;

import com.app.exceptions.UserNotFoundException;
import com.app.payloads.LoginCredentials;
import com.app.payloads.UserDTO;
import com.app.payloads.response.ApiResponse;
import com.app.payloads.response.LoginResponse;
import com.app.security.JWTUtil;
import com.app.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
// @SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "1. User Reg & SignIn API")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody UserDTO user)
            throws UserNotFoundException {
        UserDTO userDTO = userService.registerUser(user);
        ApiResponse<LoginResponse> response = jwtUtil.generateToken(userDTO.getEmail());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginCredentials credentials) {
        UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                credentials.getEmail(), credentials.getPassword());
        authenticationManager.authenticate(authCredentials);
        ApiResponse<LoginResponse> response = jwtUtil.generateToken(credentials.getEmail());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
