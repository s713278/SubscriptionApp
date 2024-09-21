package com.app.controllers;

import com.app.entites.Customer;
import com.app.exceptions.UserNotFoundException;
import com.app.payloads.CustomerDTO;
import com.app.payloads.request.ForgotPasswordRequest;
import com.app.payloads.request.OtpVerificationRequest;
import com.app.payloads.request.RefreshTokenRequest;
import com.app.payloads.request.ResendOtpRequest;
import com.app.payloads.request.ResetPasswordRequest;
import com.app.payloads.request.SignInRequest;
import com.app.payloads.request.SignUpRequest;
import com.app.payloads.response.AppResponse;
import com.app.payloads.response.SignInResponse;
import com.app.security.RefreshTokenService;
import com.app.security.TokenService;
import com.app.services.AuthService;
import com.app.services.EmailService;
import com.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/auth")
// @SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "1. User Reg & SignIn API")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final TokenService tokenService;
    private final EmailService emailService;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(description = "Customer Full Profile Registration")
    @PostMapping("/register")
    public ResponseEntity<AppResponse<SignInResponse>> register(@Valid @RequestBody CustomerDTO user)
            throws UserNotFoundException {
        String email = user.getEmail().trim().toLowerCase();
        user.setEmail(email);
        CustomerDTO userDTO = userService.registerUser(user);
        String accessToken = tokenService.generateToken(userDTO.getEmail());

        String refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        SignInResponse loginResponse = SignInResponse.builder().userToken(accessToken).refreshToken(refreshToken)
                .build();

        return new ResponseEntity<>(AppResponse.success(HttpStatus.CREATED.value(), loginResponse), HttpStatus.CREATED);
    }

    @Operation(description = "Customer SignIn")
    @PostMapping("/signin")
    public ResponseEntity<AppResponse<SignInResponse>> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        log.info("Received sign-in request for email: {}", signInRequest.getEmail());
        UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(), signInRequest.getPassword());
        var authentication = authenticationManager.authenticate(authCredentials);
        String accessToken = tokenService.generateToken(signInRequest.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(signInRequest.getEmail());
        SignInResponse loginResponse = SignInResponse.builder().userToken(accessToken).refreshToken(refreshToken)
                .build();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(AppResponse.success(HttpStatus.OK.value(), loginResponse), HttpStatus.OK);
    }

    @Operation(summary = "Customer sign-up", description = "Registers a new user by providing their first name, email, mobile number, and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request due to validation errors", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))) })
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer registration request", required = true, content = @Content(schema = @Schema(implementation = SignUpRequest.class))) @Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            log.info("Received sign-up request for email: {}", signUpRequest.getEmail());
            String response = authService.signUp(signUpRequest);
            var apiResponse = AppResponse.success(HttpStatus.CREATED.value(), response);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error during sign-up for email: {}: {}", signUpRequest.getEmail(), e.getMessage());
            throw e;
        }
    }

    @Operation(description = "Customer OTP Veriifcation")
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerificationRequest otpRequest) {
        try {
            String response = authService.verifyOtp(otpRequest);
            var apiResponse = AppResponse.success(HttpStatus.OK.value(), response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Error during otp-verification for email: {}: {}", otpRequest.getEmail(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody ResendOtpRequest resendOtpRequest) {
        return null;
        // Logic to resend OTP, similar to signUp OTP generation
    }

    // REST API to get authenticated user details (for testing session management)
    @Operation(description = "Logged-In User")
    @GetMapping("/me")
    public @ResponseBody String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return "Authenticated user: " + authentication.getName();
        } else {
            return "No authenticated user";
        }
    }

    @Operation(description = "Refresh Token")
    @PostMapping("/refresh")
    public @ResponseBody ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (refreshTokenService.validateRefreshToken(refreshToken)) {
            String newAccessToken =  tokenService.generateToken(refreshTokenService.getUserIdFromRefreshToken(refreshToken));
            return new ResponseEntity<>(AppResponse.success(HttpStatus.OK.value(), newAccessToken), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        }
    }
    

    // Account activation endpoint
    @Operation(description = "Account Access Actication")
    @GetMapping("/activate/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String token) {
        boolean isActivated = authService.activateAccount(token);
        if (isActivated) {
            return ResponseEntity.ok("Account activated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired activation token.");
        }
    }
    
 // Forgot password endpoint
    @Operation(description = " Forgot password endpoint")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Customer customer = authService.generateResetToken(request.getEmail());
        if (customer != null) {
            emailService.sendResetPasswordEmail(customer.getEmail(), customer.getResetPasswordToken());
            return ResponseEntity.ok("Password reset link has been sent to your email.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }

    // Reset password endpoint
    @Operation(description = "Reset password endpoint")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean isReset = authService.resetPassword(request.getToken(), request.getNewPassword());
        if (isReset) {
            return ResponseEntity.ok("Password reset successful.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset token.");
    }
}
