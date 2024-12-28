package com.app.controllers;

import com.app.config.AppConstants;
import com.app.constants.NotificationType;
import com.app.constants.SignInType;
import com.app.controllers.validator.AbstractRequestValidation;
import com.app.entites.Customer;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.*;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.AuthDetailsDTO;
import com.app.services.ServiceManager;
import com.app.services.auth.signin.SignInContext;
import com.app.services.auth.signup.UserSignUpStrategy;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/v1/auth")
@Tag(
    name = "1. User Authentication API",
    description = "APIs for User sing up, sign in and session management.")
public class AuthController extends AbstractRequestValidation {

  private final ServiceManager serviceManager;
  private final UserSignUpStrategy signUpStrategy;
  private final SignInContext signInContext;

  @Operation(
      summary = "Signup with mobile number",
      description =
          "Registers a new user by providing their first name, mobile number, and password.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Customer registered successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request due to validation errors",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponse.class)))
      })
  // @PostMapping("/signup")
  public ResponseEntity<APIResponse<?>> mobileSignUp(
      @Valid @RequestBody MobileSignUpRequest signUpRequest) {
    log.info("Received sign-up request for mobile: {}", signUpRequest.getMobile());

    var response = signUpStrategy.processUserSignUp(signUpRequest);
    var appResponse = APIResponse.success(HttpStatus.CREATED.value(), response);
    return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
  }

  @Operation(summary = "SignIn with mobile number")
  // @PostMapping("/signin")
  public ResponseEntity<APIResponse<?>> signIn(@Valid @RequestBody SignInRequest signInRequest) {
    log.info("Received sign-in request for mobile: {}", signInRequest.getMobile());
    AuthDetailsDTO signInResponse =
        signInContext.processSignIn(SignInType.WITH_PASSWORD, signInRequest);
    if (signInResponse.isMobileVerified()) {
      return new ResponseEntity<>(APIResponse.success(signInResponse), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          APIResponse.success("OTP sent on registered mobile,Please verify with it!"),
          HttpStatus.OK);
    }
  }

  @Operation(
      summary = "Signup with Email",
      description = "Registers a new user by providing their first name, email, and password.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "User registered successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request due to validation errors",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponse.class)))
      })
  // @PostMapping("/signup/email")
  public ResponseEntity<APIResponse<?>> signUp(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Customer registration request",
              required = true,
              content = @Content(schema = @Schema(implementation = SignUpRequest.class)))
          @Valid
          @RequestBody
          EmailSignUpRequest signUpRequest) {
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

  @Operation(summary = "Verify Email OTP")
  // TODO:  @PostMapping("/email/verify-otp")
  public ResponseEntity<?> verifyEmailOtp(@Valid @RequestBody OTPVerificationRequest otpRequest) {
    try {
      String response = serviceManager.getAuthService().verifyEmailOtp(otpRequest);
      var apiResponse = APIResponse.success(HttpStatus.OK.value(), response);
      return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      log.error(
          "Error during otp-verification for email: {}: {}", otpRequest.getEmail(), e.getMessage());
      throw new APIException(APIErrorCode.OTP_VERIFICATION_FAILED, e.getMessage());
    }
  }

  @Operation(summary = "Request OTP")
  @PostMapping("/request-otp")
  public ResponseEntity<APIResponse<?>> requestOTP(
      @RequestBody @Valid MobileSignUpRequest request, BindingResult bindingResult) {
    log.info("Received OTP request for mobile: {}", request.getMobile());
    validateRequest(bindingResult);
    var response = signUpStrategy.processUserSignUp(request);
    var appResponse = APIResponse.success(HttpStatus.CREATED.value(), response);
    return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
  }

  @Operation(summary = "Verify OTP")
  @PostMapping("/verify-otp")
  public ResponseEntity<APIResponse<?>> verifyMobileOtp(
      @Valid @RequestBody OTPVerificationRequest otpRequest) {
    SignInRequest request = new SignInRequest();
    request.setCountryCode(otpRequest.getCountryCode());
    request.setMobile(otpRequest.getMobile());
    request.setPassword(otpRequest.getOtp());
    AuthDetailsDTO signInResponse = signInContext.processSignIn(SignInType.WITH_OTP, request);
    var apiResponse = APIResponse.success(HttpStatus.OK.value(), signInResponse);
    return new ResponseEntity<>(apiResponse, HttpStatus.OK);
  }

  // REST API to get authenticated user details (for testing session management)
  @Operation(summary = "Authenticated Profile Info")
  @GetMapping("/profile")
  @SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
  public @ResponseBody ResponseEntity<String> getAuthenticatedUser(
      @AuthenticationPrincipal Long userId, Authentication authentication) {
    log.debug("authentication : {}", authentication);
    if (userId != null) {
      return new ResponseEntity<>(
          "Its authenticated request for the user :" + userId, HttpStatus.OK);
    } else {
      return new ResponseEntity<>("No authenticated user", HttpStatus.OK);
    }
  }

  @Operation(summary = "Refresh Token")
  @PostMapping("/refresh")
  public @ResponseBody ResponseEntity<APIResponse<?>> refreshAccessToken(
      @RequestBody RefreshTokenRequest refreshTokenRequest) {
    String refreshToken = refreshTokenRequest.getRefreshToken();
    var refreshTokenService = serviceManager.getRefreshTokenService();
    if (!refreshTokenService.validateRefreshToken(refreshToken)) {
      throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid refresh token");
    }
    String newAccessToken =
        serviceManager
            .getTokenService()
            .generateToken(refreshTokenService.getUserIdFromRefreshToken(refreshToken));
    return new ResponseEntity<>(APIResponse.success(newAccessToken), HttpStatus.OK);
  }

  @Operation(summary = "Sign out")
  @PostMapping("/signout")
  public @ResponseBody ResponseEntity<APIResponse<?>> signOut(
      @RequestBody RefreshTokenRequest refreshTokenRequest) {
    String refreshToken = refreshTokenRequest.getRefreshToken();
    var refreshTokenService = serviceManager.getRefreshTokenService();
    if (!refreshTokenService.validateRefreshToken(refreshToken)) {
      throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid refresh token");
    }
    refreshTokenService.removeRefreshToken(refreshToken);
    return new ResponseEntity<>(APIResponse.success("Successfully logged out."), HttpStatus.OK);
  }

  // Account activation endpoint
  @Operation(summary = "Activation with Token")
  // TODO:  @GetMapping("/activate/{token}")
  public ResponseEntity<?> activateAccount(@PathVariable String token) {
    boolean isActivated = serviceManager.getAuthService().activateAccount(token);
    if (isActivated) {
      return ResponseEntity.ok("Account activated successfully.");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Invalid or expired activation token.");
    }
  }

  // Forgot password endpoint
  @Operation(summary = "Forgot Password")
  // @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
    Customer customer = serviceManager.getAuthService().generateResetToken(request.getEmail());
    if (customer != null) {
      serviceManager
          .getNotificationContext()
          .sendResetPasswordEmail(
              NotificationType.EMAIL, customer.getEmail(), customer.getResetPasswordToken());
      return ResponseEntity.ok("Password reset link has been sent to your email.");
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
  }

  // Reset password endpoint
  @Operation(summary = "Reset Password")
  // @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
    boolean isReset =
        serviceManager.getAuthService().resetPassword(request.getToken(), request.getNewPassword());
    if (isReset) {
      return ResponseEntity.ok("Password reset successful.");
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset token.");
  }
}
