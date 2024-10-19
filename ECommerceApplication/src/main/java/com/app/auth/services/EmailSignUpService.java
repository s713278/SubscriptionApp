package com.app.auth.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.config.AppConstants;
import com.app.config.GlobalConfig;
import com.app.entites.Customer;
import com.app.entites.Role;
import com.app.event.EmailActivationEvent;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.EmailSignUpRequest;
import com.app.payloads.response.SignUpDTO;
import com.app.repositories.RepositoryManager;

@Transactional
@Service
public class EmailSignUpService extends AbstractSignUp<EmailSignUpRequest> {
    // Email validation regex pattern
    private static final String EMAIL_PATTERN = "^(?!.*\\.\\.)(?!.*\\.$)(?!^\\.)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    public EmailSignUpService(RepositoryManager repoManager, PasswordEncoder passwordEncoder,
            GlobalConfig globalConfig, ApplicationEventPublisher eventPublisher, OTPService otpService) {
        this.globalConfig = globalConfig;
        this.repoManager = repoManager;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.otpService = otpService;
    }

    @Override
    protected void preSignUpOperations(EmailSignUpRequest request) {
        super.preSignUpOperations(request);
        // Perform mobile-specific pre-sign-up operations, like OTP validation
        if (!isValidEmail(request.getEmail())) {
            throw new APIException(APIErrorCode.API_400, "Invalid email address!");
        }
        // Optionally check if mobile number already exists
        if (isEmailRegistered(request.getEmail())) {
            throw new APIException(APIErrorCode.API_400, "Email is already registered!");
        }
       
    }

    @Override
    protected void postSignUpOperations(EmailSignUpRequest request) {
        super.postSignUpOperations(request);
        // Publish a sign-up event asynchronously
        EmailActivationEvent activationEvent = new EmailActivationEvent(this, request.getEmail(), request.getEmailActivationtoken(),request.getOtp());
        eventPublisher.publishEvent(activationEvent);
    }

    @Transactional
    @Override
    protected SignUpDTO doSignUp(EmailSignUpRequest request) {
        // Create a new user
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword())); // Use BCrypt for password encryption

        // Fetch the role and ensure it is managed
        Role role = repoManager.getRoleRepo().findById(AppConstants.USER_ROLE_ID)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        customer.getRoles().add(role);

        // Generate OTP
        String otp = otpService.generateOtp(request.getEmail());
        customer.setOtp(otp);
        customer.setOtpExpiration(LocalDateTime.now().plusMinutes(15)); // Set OTP expiration to 5 minutes
        customer.setOtpExpiration(LocalDateTime.now().plusMinutes(5)); // Set OTP expiration to 5 minutes
        customer.setDeliveryAddress(Collections.emptyMap());
        
        String activationToken = UUID.randomUUID().toString();
        customer.setEmailActivationToken(activationToken); // Generate a random activation token
        customer.setEmailTokenExpiration(LocalDateTime.now().plusSeconds(globalConfig.getCustomerConfig().getEmailTokenExp())); // Set token expiration time
        customer = repoManager.getCustomerRepo().save(customer);
        request.setEmailActivationtoken(activationToken);
        request.setOtp(otp);
        return new SignUpDTO(customer.getId(),"Email registered successdully!");
    }

    private boolean isValidEmail(String email) {
         // Compile the pattern
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        // Match the given email against the pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isEmailRegistered(String email) {
        return repoManager.getCustomerRepo().findByEmail(email).isPresent();
    }
}
