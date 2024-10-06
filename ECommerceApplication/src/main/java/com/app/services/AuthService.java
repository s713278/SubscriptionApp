package com.app.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.config.AppConstants;
import com.app.config.GlobalConfig;
import com.app.entites.Customer;
import com.app.entites.Role;
import com.app.event.CustomerSignUpEvent;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.request.OtpVerificationRequest;
import com.app.payloads.request.SignUpRequest;
import com.app.repositories.CustomerRepo;
import com.app.repositories.RoleRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepo customerRepo;
    
    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;

   private final GlobalConfig globalConfig;
   
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public String signUp(SignUpRequest request) {
        // Check if the user already exists
        Optional<Customer> existingUser = Optional.empty();
        if (request.getEmail() != null) {
            existingUser = customerRepo.findByEmail(request.getEmail());
        } else if (request.getMobile() != null) {
            existingUser = customerRepo.findByMobile(Long.parseLong(request.getMobile()));
        }

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        // Create a new user
        Customer user = new Customer();
        user.setFirstName(request.getFirstName());
        user.setEmail(request.getEmail());
       // user.setMobile(Long.parseLong(request.getMobile()));
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Use BCrypt for password encryption
        
     // Fetch the role and ensure it is managed
        Role role = roleRepo.findById(AppConstants.USER_ROLE_ID)
                            .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        
        user.getRoles().add(role);

        // Generate OTP
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(5)); // Set OTP expiration to 5 minutes
        user.setDeliveryAddress(Collections.emptyMap());

        user.setEmailActivationToken(UUID.randomUUID().toString()); // Generate a random activation token
        user.setEmailTokenExpiration(LocalDateTime.now().plusSeconds(globalConfig.getCustomerConfig().getEmailTokenExp())); // Set token expiration time
        
        customerRepo.save(user);

     // Publish a sign-up event asynchronously
        CustomerSignUpEvent signUpEvent = new CustomerSignUpEvent(
            this,
            request.getEmail(),
            request.getMobile(),
            user.getEmailActivationToken(),
            otp
        );
        eventPublisher.publishEvent(signUpEvent);
        return "Customer registered successfully and OTP sent for verification!!";
    }

    public String verifyOtp(OtpVerificationRequest request) {
        // Find the user by email
        Optional<Customer> userOptional = customerRepo.findByEmail(request.getEmail());
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

    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // Generates 6-digit OTP
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
}
