package com.app.services;

import com.app.config.AppConstants;
import com.app.entites.Customer;
import com.app.entites.Role;
import com.app.event.CustomerSignUpEvent;
import com.app.payloads.request.OtpVerificationRequest;
import com.app.payloads.request.SignUpRequest;
import com.app.repositories.CustomerRepo;
import com.app.repositories.RoleRepo;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private final CustomerRepo customerRepo;
    
    @Autowired
    private final RoleRepo roleRepo;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final EmailService emailService; // Or SMS service for mobile OTP
    
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
        user.setMobile(Long.parseLong(request.getMobile()));
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

        customerRepo.save(user);

     // Publish a sign-up event asynchronously
        CustomerSignUpEvent signUpEvent = new CustomerSignUpEvent(
            this,
            request.getEmail(),
            request.getMobile(),
            request.getFirstName(),
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
        user.setVerified(true);
        user.setOtp(null); // Clear the OTP
        user.setOtpExpiration(null);

        customerRepo.save(user);

        return "OTP verified successfully.";
    }

    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // Generates 6-digit OTP
    }
}
