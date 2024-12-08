package com.app.services.auth.signup;

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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
            customer.setCountryCode(request.getCountryCode());
            customer.setMobile(request.getMobile());
            customer.setRegSource(request.getRegSource());
            customer.setPassword(passwordEncoder.encode(request.getPassword())); // Use BCrypt for password encryption

            // Fetch the role and ensure it is managed
            Role role = repoManager.getRoleRepo().findById(AppConstants.USER_ROLE_ID)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found"));
            customer.getRoles().add(role);
            // Generate OTP
           // String otp = otpService.generateOtp(100L);
            //customer.setOtp(otp);
            customer.setActive(true);
            customer.setMobileVerified(false);
            customer.setOtpExpiration(LocalDateTime.now().plusMinutes(15)); // Set OTP expiration to 5 minutes
            customer= serviceManager.getUserService().createUser(customer);
           // customer = repoManager.getCustomerRepo().save(customer);
            return new SignUpDTO(customer.getId(),customer.getFullMobileNumber(),customer.getMobileVerified(),customer.getEmailVerified(),
                    "OTP Sent to mobile number , Please verify with it!");
        }catch (Exception e){
            log.error("Error occurred while creating new user with mobile number : {}",request.getMobile(),e);
            throw new APIException(APIErrorCode.USER_CREATION_FAILED, e.getMessage());
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
