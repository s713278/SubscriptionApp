package com.app.auth.services.signup;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.config.AppConstants;
import com.app.config.GlobalConfig;
import com.app.entites.Customer;
import com.app.entites.Role;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.MobileSignUpRequest;
import com.app.payloads.response.SignUpDTO;
import com.app.repositories.RepositoryManager;
import com.app.services.ServiceManager;

import lombok.extern.slf4j.Slf4j;

@Service(value = "mobileSignUpService")
@Slf4j
public class MobileSignUpService extends AbstractSignUpService<MobileSignUpRequest> {
    public MobileSignUpService(RepositoryManager repoManager, PasswordEncoder passwordEncoder,
            GlobalConfig globalConfig, ApplicationEventPublisher eventPublisher, ServiceManager serviceManager) {
        this.globalConfig = globalConfig;
        this.repoManager = repoManager;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.serviceManager = serviceManager;
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
            throw new APIException(APIErrorCode.API_409, "Mobile number is already registered!");
        }
    }


    @Override
    protected void postSignUpOperations(SignUpDTO signUpDTO) {
        super.postSignUpOperations(signUpDTO);
        // Publish a sign-up event asynchronously
       // String otp =otpService.generateOtp(String.valueOf(signUpDTO.mobile()));
       // MobileActivationEvent signUpEvent = new MobileActivationEvent(this, signUpDTO.mobile(), otp);
        //eventPublisher.publishEvent(signUpEvent);
        //log.debug("OTP is generated for user : {}"
          //      ,signUpDTO.userId()
        //);
    }

    @Transactional
    @Override
    protected SignUpDTO doSignUp(MobileSignUpRequest request) {
        // Create a new user
        try {
            Customer customer = new Customer();
            customer.setFirstName(request.getFirstName());
            customer.setMobile(request.getMobile());
            customer.setPassword(passwordEncoder.encode(request.getPassword())); // Use BCrypt for password encryption

            // Fetch the role and ensure it is managed
            Role role = repoManager.getRoleRepo().findById(AppConstants.USER_ROLE_ID)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found"));
            customer.getRoles().add(role);
            // Generate OTP
           // String otp = otpService.generateOtp(100L);
            //customer.setOtp(otp);
            customer.setActive(true);
            if(globalConfig.getCustomerConfig().isOtpVerificationEnabled()) {
                customer.setMobileVerified(false);
            }
            else{
                customer.setMobileVerified(true);
            }
            customer.setOtpExpiration(LocalDateTime.now().plusMinutes(15)); // Set OTP expiration to 5 minutes
          //  request.setOtp(otp);
            customer = repoManager.getCustomerRepo().save(customer);
            return new SignUpDTO(customer.getId(),customer.getMobile(),customer.getMobileVerified(),customer.getEmailVerified(), "Mobile registered successfully!");
        }catch (Exception e){
            log.error("Error occurred while creating new user with mobile number : {}",request.getMobile(),e);
            throw new APIException(APIErrorCode.API_417, e.getMessage());
        }
    }

    private boolean isValidMobileNumber(Long mobile) {
        // Mobile number validation logic
        return mobile != null && String.valueOf(mobile).matches("[0-9]{10}");
    }

    private boolean isMobileNumberRegistered(Long mobile) {
        return repoManager.getCustomerRepo().findByMobile(mobile).isPresent();
    }
}
