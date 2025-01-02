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
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "mobileSignUpService")
@Slf4j
public class MobileSignUpService extends AbstractSignUpService<MobileSignUpRequest> {
  public MobileSignUpService(
      RepositoryManager repoManager,
      PasswordEncoder passwordEncoder,
      GlobalConfig globalConfig,
      ApplicationEventPublisher eventPublisher,
      ServiceManager serviceManager) {
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
      throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid mobile number.");
    }
    if (!AppConstants.COUNTRY_CODES.contains(request.getCountryCode())) {
      throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid country code.");
    }
    // Optionally check if mobile number already exists
    // if (isMobileNumberRegistered(request.getMobile())) {
    // throw new APIException(APIErrorCode.API_409, "Mobile number is already registered!");
    //   log.debug("User already registered.");
    // }
  }

  @Override
  protected void postSignUpOperations(SignUpDTO signUpDTO) {
    super.postSignUpOperations(signUpDTO);
    // Publish a sign-up event asynchronously
    // String otp =otpService.generateOtp(String.valueOf(signUpDTO.mobile()));
    // MobileActivationEvent signUpEvent = new MobileActivationEvent(this, signUpDTO.mobile(), otp);
    // eventPublisher.publishEvent(signUpEvent);
    // log.debug("OTP is generated for user : {}"
    //      ,signUpDTO.userId()
    // );
  }

  @Transactional
  @Override
  protected SignUpDTO doSignUp(MobileSignUpRequest request) {
    // Create a new user
    Long roleId = AppConstants.ADMIN_ROLE_ID;
    Customer customer = new Customer();
    try {
      switch (request.getUserRoleEnum()) {
        case ADMIN -> roleId = AppConstants.ADMIN_ROLE_ID;
        case VENDOR -> roleId = AppConstants.VENDOR_ROLE_ID;
        case CUSTOMER_CARE -> roleId = AppConstants.CC_ROLE_ID;
        case null, default -> roleId = AppConstants.USER_ROLE_ID;
      }
      var optionalUser =
          repoManager.getCustomerRepo().findUserByMobile(Long.parseLong(request.getMobile()));
      log.debug("User existed by mobile number ? {}", optionalUser.isPresent());
      // Fetch the role and ensure it is managed
      Role role =
          repoManager
              .getRoleRepo()
              .findById(roleId)
              .orElseThrow(() -> new IllegalArgumentException("Role not found"));
      if (optionalUser.isEmpty()) {
        customer.setCountryCode(request.getCountryCode());
        customer.setMobile(Long.parseLong(request.getMobile()));
        customer.setRegSource(request.getRegPlatform().name());
        customer.getRoles().add(role);
        customer.setPassword(String.valueOf(request.getMobile()).substring(0, 5));
        customer.setActive(true);
        customer.setMobileVerified(false);
        customer.setOtpExpiration(
            LocalDateTime.now().plusMinutes(15)); // Set OTP expiration to 5 minutes
      } else {
        customer = optionalUser.get();
        // customer.getRoles().add(role);
      }
      customer = serviceManager.getUserService().createUser(customer);
      log.info("User #{} is created for mobile :{}", customer.getId(), request.getMobile());
    } catch (Exception e) {
      log.error(
          "Error occurred while creating new user with mobile number : {}", request.getMobile(), e);
      throw new APIException(APIErrorCode.USER_CREATION_FAILED, e.getMessage());
    }
    return new SignUpDTO(
        customer.getId(), customer.getFullMobileNumber(), customer.getMobileVerified());
  }

  private boolean isValidMobileNumber(String mobile) {
    // Mobile number validation logic
    return mobile != null && mobile.matches(AppConstants.MOBILE_REGEX);
  }
}
