package com.app.auth.services;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.config.AppConstants;
import com.app.config.GlobalConfig;
import com.app.entites.Customer;
import com.app.entites.Role;
import com.app.event.MobileActivationEvent;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.MobileSignUpRequest;
import com.app.payloads.response.SignUpResponse;
import com.app.repositories.RepositoryManager;

@Transactional
@Service
public class MobileSignUpService extends AbstractSignUp<MobileSignUpRequest> {

    public MobileSignUpService(RepositoryManager repoManager, PasswordEncoder passwordEncoder,
            GlobalConfig globalConfig, ApplicationEventPublisher eventPublisher, OTPService otpService) {
        this.globalConfig = globalConfig;
        this.repoManager = repoManager;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.otpService = otpService;
    }

    @Override
    protected void preSignUpOperations(MobileSignUpRequest request) {
        super.preSignUpOperations(request);
        // Perform mobile-specific pre-sign-up operations, like OTP validation
        if (!isValidMobileNumber(request.getMobile())) {
            throw new APIException(APIErrorCode.API_400, "Invalid mobile number");
        }
        // Optionally check if mobile number already exists
        if (isMobileNumberRegistered(request.getMobile())) {
            throw new APIException(APIErrorCode.API_400, "Mobile number is already registered!");
        }
    }

    @Async
    @Override
    protected void postSignUpOperations(MobileSignUpRequest request) {
        super.postSignUpOperations(request);
        // Publish a sign-up event asynchronously
        MobileActivationEvent signUpEvent = new MobileActivationEvent(this, request.getMobile(), request.getOtp());
        eventPublisher.publishEvent(signUpEvent);
    }

    @Transactional
    @Override
    protected SignUpResponse doSignUp(MobileSignUpRequest request) {
        
        // Create a new user
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setMobile(request.getMobile());
        customer.setPassword(passwordEncoder.encode(request.getPassword())); // Use BCrypt for password encryption

        // Fetch the role and ensure it is managed
        Role role = repoManager.getRoleRepo().findById(AppConstants.USER_ROLE_ID)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        customer.getRoles().add(role);

        // Generate OTP
        String otp = otpService.generateOtp();
        customer.setOtp(otp);
        customer.setOtpExpiration(LocalDateTime.now().plusMinutes(15)); // Set OTP expiration to 5 minutes
        request.setOtp(otp);
        customer = repoManager.getCustomerRepo().save(customer);
        return new SignUpResponse(customer.getId(),"Mobile registered successdully!");
    }

    private boolean isValidMobileNumber(Long mobile) {
        // Mobile number validation logic
        return mobile != null && String.valueOf(mobile).matches("[0-9]{10}");
    }

    private boolean isMobileNumberRegistered(Long mobile) {
        return repoManager.getCustomerRepo().findByMobile(mobile).isPresent();
    }
}
