package com.app.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.config.GlobalConfig;
import com.app.constants.NotificationType;
import com.app.entites.Customer;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.request.OTPRequest;
import com.app.payloads.request.OTPVerificationRequest;
import com.app.repositories.CustomerRepo;
import com.app.security.RefreshTokenService;
import com.app.security.TokenService;
import com.app.services.auth.dto.AuthUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final GlobalConfig globalConfig;
    private final ServiceManager serviceManager;

    private Customer preRequestOTP(final Long mobileNo){
        return serviceManager.getUserService().isMobileNumberRegistered(mobileNo);
    }
    private void postRequestOTP( Customer user){

    }
    public void requestOTP(final OTPRequest request){
        Customer user = preRequestOTP(request.mobile());
        serviceManager.getNotificationService().sendOTPMessage(NotificationType.SMS,
                ""+request.mobile());
        postRequestOTP(user);
    }



    public String verifyEmailOtp(OTPVerificationRequest request) {
        // Find the user by email
        Optional<Customer> userOptional = customerRepo.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
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


   @Async
    private void postSignIn(AuthUserDetails userDetails){
    if(userDetails.isMobileVerified()){
        //TODO : Last Logged In
    }else{
        log.debug("Sending OTP to mobile :{} for User : {}",userDetails.getMobile(),userDetails.getId());
        serviceManager.getNotificationService().sendOTPMessage(NotificationType.SMS,
                ""+userDetails.getMobile()
                ,serviceManager.getOtpService().generateOtp(""+userDetails.getId()));
    }
    }

}
