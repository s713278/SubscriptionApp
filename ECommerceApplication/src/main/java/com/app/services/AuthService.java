package com.app.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.auth.dto.AuthUserDetails;
import com.app.auth.services.OTPService;
import com.app.config.GlobalConfig;
import com.app.entites.Customer;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.request.OTPVerificationRequest;
import com.app.payloads.response.AuthDetailsDTO;
import com.app.repositories.CustomerRepo;
import com.app.security.RefreshTokenService;
import com.app.security.TokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final GlobalConfig globalConfig;
    private final OTPService otpService;
    public String verifyMobileOtp(OTPVerificationRequest request) {
        // Find the user by email
        Customer user = customerRepo.findByEmail(request.getEmailOrMobile()).orElseThrow(() -> new APIException(APIErrorCode.API_401,"User not found in system"));

        otpService.verifyOtp(request.getEmailOrMobile(),request.getOtp());
        // Check OTP and expiration
        if (!user.getOtp().equals(request.getOtp())) {
            throw new IllegalArgumentException("Invalid OTP");
        }
        if (user.getOtpExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired");
        }

        // Mark user as verified
        // user.setVerified(true);
        user.setMobileVerified(true); // Clear the OTP
        user.setOtpExpiration(null);
        customerRepo.save(user);
        return "Mobile OTP verification is success.";
    }

    public String verifyOtp(OTPVerificationRequest request) {
        // Find the user by email
        Optional<Customer> userOptional = customerRepo.findByEmail(request.getEmailOrMobile());
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

        Customer user = userOptional.get();
        // Check OTP and expiration
        if (!user.getOtp().equals(request.getOtp())) {
            throw new IllegalArgumentException("Invalid OTP");
        }
        if (user.getOtpExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired");
        }

        // Mark user as verified
       // user.setVerified(true);
        user.setOtp(null); // Clear the OTP
        user.setOtpExpiration(null);
        customerRepo.save(user);
        return "OTP verified successfully.";
    }

    // Activate user account
    public boolean activateAccount(final String token) {
        Customer customer = customerRepo.findByEmailActivationToken(token);
        if (customer != null && customer.getEmailTokenExpiration().isAfter(LocalDateTime.now())) {
            customer.setEmailVerified(true);
            customer.setEmailActivationToken(null); // Clear the token after activation
            customer.setEmailTokenExpiration(null);
            customerRepo.save(customer);
            return true;
        }
        return false;
    }
    

    // Generate reset password token
    public Customer generateResetToken(String email) {
        Customer customer = customerRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException());
        if (customer!=null) {
            customer.setResetPasswordToken(UUID.randomUUID().toString());
            customer.setEmailTokenExpiration(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
            customerRepo.save(customer);
        }
        return customer;
    }
    
 // Reset password
    public boolean resetPassword(String token, String newPassword) {
        Customer customer = customerRepo.findByResetPasswordToken(token);
        if (customer != null && customer.getEmailTokenExpiration().isAfter(LocalDateTime.now())) {
            customer.setPassword(passwordEncoder.encode(newPassword)); // Set new encoded password
            customer.setResetPasswordToken(null); // Clear the token
            customer.setEmailTokenExpiration(null);
            customerRepo.save(customer);
            return true;
        }
        return false;
    }

    public AuthDetailsDTO getSignInResponse(AuthUserDetails userDetails){
        var responseBuilder = AuthDetailsDTO.builder();
        if(!globalConfig.getCustomerConfig().isOtpVerificationEnabled() || userDetails.isMobileVerified()){
            String accessToken = tokenService.generateToken(userDetails);
            String refreshToken = refreshTokenService.createRefreshToken(userDetails);
            return responseBuilder.userToken(accessToken)
                    .refreshToken(refreshToken)
                    .activeSubscriptions(List.of())
                    .userId(userDetails.getId()).build();
        }else{
            return responseBuilder.message("Mobile number is not verified,Please verify")
                    .userId(userDetails.getId()).build();
        }

    }
}
