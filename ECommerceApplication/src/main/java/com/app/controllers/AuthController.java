package com.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.app.auth.dto.AuthUserDetails;
import com.app.auth.services.otp.SendOTPStrategy;
import com.app.auth.services.signup.UserSignUpStrategy;
import com.app.entites.Customer;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.EmailSignUpRequest;
import com.app.payloads.request.ForgotPasswordRequest;
import com.app.payloads.request.MobileSignInRequest;
import com.app.payloads.request.MobileSignUpRequest;
import com.app.payloads.request.OTPRequest;
import com.app.payloads.request.OTPVerificationRequest;
import com.app.payloads.request.RefreshTokenRequest;
import com.app.payloads.request.ResetPasswordRequest;
import com.app.payloads.request.SignUpRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.AuthDetailsDTO;
import com.app.security.RefreshTokenService;
import com.app.security.TokenService;
import com.app.services.AuthService;
import com.app.services.ServiceManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/v1/auth")
@Tag(name = "1. User Authentication")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
   // private final SignUpStrategyFactory signUpStrategyFactory;
    private final ServiceManager serviceManager;
    private final AuthenticationManager authenticationManager;
    private final SendOTPStrategy otpStrategy;
    private final UserSignUpStrategy signUpStrategy;

    @Operation(summary = "Signup with mobile number", description = "Registers a new user by providing their first name, mobile number, and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request due to validation errors", content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))) })
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<?>> mobileSignUp(@Valid @RequestBody MobileSignUpRequest signUpRequest) {
            log.info("Received sign-up request for mobile: {}", signUpRequest.getMobile());
           /* SignUpStrategy<MobileSignUpRequest> signUpStrategy = signUpStrategyFactory
                    .getStrategy("mobileSignUpStrategy");

            var response = signUpStrategy.signUp(signUpRequest);*/
        var response = signUpStrategy.processUserSignUp(signUpRequest);
            var appResponse = APIResponse.success(HttpStatus.CREATED.value(), response);
            return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
    }

    @Operation(summary  = "SignIn with mobile number")
    @PostMapping("/signin")
    public ResponseEntity<APIResponse<?>> signIn(@Valid @RequestBody MobileSignInRequest signInRequest) {
        log.info("Received sign-in request for mobile: {}", signInRequest.getMobile());
        UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                signInRequest.getMobile(), signInRequest.getPassword());
        var authentication = authenticationManager.authenticate(authCredentials);
        var userDetails = (AuthUserDetails) authentication.getPrincipal();
        AuthDetailsDTO signInResponse =authService.getSignInResponse(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(APIResponse.success(signInResponse), HttpStatus.OK);
    }

    @Operation(summary = "Signup with Email", description = "Registers a new user by providing their first name, email, and password.")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request due to validation errors", content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))) })
    //@PostMapping("/signup/email")
    public ResponseEntity<APIResponse<?>> signUp(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer registration request", required = true, content = @Content(schema = @Schema(implementation = SignUpRequest.class))) @Valid @RequestBody EmailSignUpRequest signUpRequest) {
        try {
            log.info("Received sign-up request for email: {}", signUpRequest.getEmail());
          //  SignUpStrategy<EmailSignUpRequest> signUpStrategy = signUpStrategyFactory
            //        .getStrategy("emailSignUpStrategy");
            var response = signUpStrategy.processUserSignUp(signUpRequest);
            var apiResponse = APIResponse.success(HttpStatus.CREATED.value(), response);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error during sign-up for email: {}: {}", signUpRequest.getEmail(), e.getMessage());
            throw e;
        }
    }

    @Operation(summary  = "Verify Mobile OTP")
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyMobileOtp(@Valid @RequestBody OTPVerificationRequest otpRequest) {
            String response = authService.verifyMobileOtp(otpRequest);
            var apiResponse = APIResponse.success(HttpStatus.OK.value(), response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary  = "Verify Email OTP")
    //TODO:  @PostMapping("/email/verify-otp")
    public ResponseEntity<?> verifyEmailOtp(@Valid @RequestBody OTPVerificationRequest otpRequest) {
        try {
            String response = authService.verifyEmailOtp(otpRequest);
            var apiResponse = APIResponse.success(HttpStatus.OK.value(), response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Error during otp-verification for email: {}: {}", otpRequest.getEmail(), e.getMessage());
            throw new APIException(APIErrorCode.API_420,e.getMessage());
        }
    }

    @Operation(summary  = "Request OTP")
    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOTP(@RequestBody OTPRequest request) {
        log.info("Received OTP request for mobile: {}", request.mobile());
        otpStrategy.sendOTP(request);
        return  ResponseEntity.ok(APIResponse.success("OTP Sent successfully"));
    }

    // REST API to get authenticated user details (for testing session management)
    @Operation(summary  = "Authenticated Profile Info")
    @GetMapping("/profile")
    @SecurityRequirement(name = "E-Commerce Application")
    public @ResponseBody String getAuthenticatedUser(@AuthenticationPrincipal Long userId) {
        if (userId != null) {
            return "Its authenticated request for the user :"+userId;
            } else {
            return "No authenticated user";
        }
    }

    @Operation(summary = "Refresh Token")
    @PostMapping("/refresh")
    public @ResponseBody ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (refreshTokenService.validateRefreshToken(refreshToken)) {
            String newAccessToken = tokenService
                    .generateToken(refreshTokenService.getUserIdFromRefreshToken(refreshToken));
            return new ResponseEntity<>(APIResponse.success(HttpStatus.OK.value(), newAccessToken), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        }
    }

    // Account activation endpoint
    @Operation(summary = "Activation with Token")
   //TODO:  @GetMapping("/activate/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String token) {
        boolean isActivated = authService.activateAccount(token);
        if (isActivated) {
            return ResponseEntity.ok("Account activated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired activation token.");
        }
    }

    // Forgot password endpoint
    @Operation(summary = "Forgot Password")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Customer customer = authService.generateResetToken(request.getEmail());
        if (customer != null) {
            serviceManager.getEmailService().sendResetPasswordEmail(customer.getEmail(), customer.getResetPasswordToken());
            return ResponseEntity.ok("Password reset link has been sent to your email.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }

    // Reset password endpoint
    @Operation(summary  = "Reset Password")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean isReset = authService.resetPassword(request.getToken(), request.getNewPassword());
        if (isReset) {
            return ResponseEntity.ok("Password reset successful.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset token.");
    }
}
