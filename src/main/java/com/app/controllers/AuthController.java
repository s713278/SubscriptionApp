package com.app.controllers;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.app.constants.NotificationType;
import com.app.constants.SignInType;
import com.app.entites.Customer;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.*;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.AuthDetailsDTO;
import com.app.services.ServiceManager;
import com.app.services.auth.signin.SignInContext;
import com.app.services.auth.signup.UserSignUpStrategy;
import com.app.services.notification.NotificationContext;

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

    private final ServiceManager serviceManager;
    private final UserSignUpStrategy signUpStrategy;
    private final SignInContext signInContext;
    private final NotificationContext notificationContext;

    @Operation(summary = "Signup with mobile number", description = "Registers a new user by providing their first name, mobile number, and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request due to validation errors", content = @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))) })
    //@PostMapping("/signup")
    public ResponseEntity<APIResponse<?>> mobileSignUp(@Valid @RequestBody MobileSignUpRequest signUpRequest) {
            log.info("Received sign-up request for mobile: {}", signUpRequest.getMobile());
        var response = signUpStrategy.processUserSignUp(signUpRequest);
            var appResponse = APIResponse.success(HttpStatus.CREATED.value(), response);
            return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
    }

    @Operation(summary  = "SignIn with mobile number")
   // @PostMapping("/signin")
    public ResponseEntity<APIResponse<?>> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        log.info("Received sign-in request for mobile: {}", signInRequest.getMobile());
        AuthDetailsDTO signInResponse = signInContext.processSignIn(SignInType.WITH_PASSWORD,signInRequest);
        if(signInResponse.isMobileVerified()){
            return new ResponseEntity<>(APIResponse.success(signInResponse), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(APIResponse.success("OTP sent on registered mobile,Please verify with it!"), HttpStatus.OK);
        }

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
            var response = signUpStrategy.processUserSignUp(signUpRequest);
            var apiResponse = APIResponse.success(HttpStatus.CREATED.value(), response);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error during sign-up for email: {}: {}", signUpRequest.getEmail(), e.getMessage());
            throw e;
        }
    }


    @Operation(summary  = "Verify Email OTP")
    //TODO:  @PostMapping("/email/verify-otp")
    public ResponseEntity<?> verifyEmailOtp(@Valid @RequestBody OTPVerificationRequest otpRequest) {
        try {
            String response = serviceManager.getAuthService().verifyEmailOtp(otpRequest);
            var apiResponse = APIResponse.success(HttpStatus.OK.value(), response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Error during otp-verification for email: {}: {}", otpRequest.getEmail(), e.getMessage());
            throw new APIException(APIErrorCode.OTP_VERIFICATION_FAILED,e.getMessage());
        }
    }

    @Operation(summary  = "Request OTP")
    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOTP(@RequestBody OTPRequest request) {
        log.info("Received OTP request for mobile: {}", request.getMobile());
        if(String.valueOf(request.getMobile()).length()!=10){
            throw new APIException(APIErrorCode.API_400,"Invalid mobile number and should be 10 digits.");
        }
        serviceManager.getUserService().createUserIfNotExisted(request);
        if(request.getMobile()!=null){
            notificationContext.sendOTPMessage(NotificationType.SMS,
                    request.getFullPhoneNumber());
        }else{
            notificationContext.sendOTPMessage(NotificationType.EMAIL,
                    request.getEmail());
        }
        return  ResponseEntity.ok(APIResponse.success("OTP Sent successfully"));
    }

    @Operation(summary  = "Verify OTP")
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyMobileOtp(@Valid @RequestBody OTPVerificationRequest otpRequest) {
        SignInRequest request = new SignInRequest();
        request.setCountryCode(otpRequest.getCountryCode());
        request.setMobile(otpRequest.getMobile());
        request.setPassword(otpRequest.getOtp());
        AuthDetailsDTO signInResponse= signInContext.processSignIn(SignInType.WITH_OTP,request);
        var apiResponse = APIResponse.success(HttpStatus.OK.value(), signInResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
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
    public @ResponseBody ResponseEntity<APIResponse<?>> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        var refreshTokenService= serviceManager.getRefreshTokenService();
        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            throw new APIException(APIErrorCode.API_400, "Invalid refresh token");
        }
            String newAccessToken = serviceManager.getTokenService()
                    .generateToken(refreshTokenService.getUserIdFromRefreshToken(refreshToken));
            return new ResponseEntity<>(APIResponse.success(newAccessToken), HttpStatus.OK);
    }

    // Account activation endpoint
    @Operation(summary = "Activation with Token")
   //TODO:  @GetMapping("/activate/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String token) {
        boolean isActivated = serviceManager.getAuthService().activateAccount(token);
        if (isActivated) {
            return ResponseEntity.ok("Account activated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired activation token.");
        }
    }

    // Forgot password endpoint
    @Operation(summary = "Forgot Password")
    //@PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Customer customer = serviceManager.getAuthService().generateResetToken(request.getEmail());
        if (customer != null) {
            serviceManager.getNotificationContext().sendResetPasswordEmail(NotificationType.EMAIL,customer.getEmail(), customer.getResetPasswordToken());
            return ResponseEntity.ok("Password reset link has been sent to your email.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
    }

    // Reset password endpoint
    @Operation(summary  = "Reset Password")
    //@PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean isReset = serviceManager.getAuthService().resetPassword(request.getToken(), request.getNewPassword());
        if (isReset) {
            return ResponseEntity.ok("Password reset successful.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset token.");
    }
   // @GetMapping("/openapi.json")
   // @GetMapping(value = "/openapi.json", produces = "application/json")
    public ResponseEntity<byte[]> getOpenApiJson() throws IOException {
        ClassPathResource resource = new ClassPathResource("openapi.json");
        byte[] data = Files.readAllBytes(resource.getFile().toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
