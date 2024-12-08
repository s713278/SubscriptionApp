package com.app.services.auth.signin;


import com.app.config.GlobalConfig;
import com.app.constants.NotificationType;
import com.app.entites.Customer;
import com.app.payloads.request.SignInRequest;
import com.app.payloads.response.AuthDetailsDTO;
import com.app.services.ServiceManager;
import com.app.services.auth.dto.AuthUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractSignInService {

    @Autowired
    protected GlobalConfig globalConfig;
    @Autowired
    protected ServiceManager serviceManager;

    public final AuthDetailsDTO processSignIn(SignInRequest request) {
        var user = preSignIn(request);
       var response= doSignIn(request,user);
        postSignIn(response);
        return response;
    }

    protected Customer preSignIn(SignInRequest request) {
        return serviceManager.getUserService().fetchUserByMobileNumber(request.getMobile());
    }

    protected abstract AuthDetailsDTO doSignIn(SignInRequest request,Customer user);

    protected void postSignIn(AuthDetailsDTO authDetailsDTO) {
       //TODO: Add UserLastLoginEntry
    }

    protected AuthDetailsDTO getSignInResponse(AuthUserDetails userDetails){
        var responseBuilder = AuthDetailsDTO.builder()
                .emailVerified(userDetails.isEmailVerified())
                .mobileVerified(userDetails.isMobileVerified())
                .userId(userDetails.getId());
        if(!globalConfig.getCustomerConfig().isOtpVerificationEnabled() || userDetails.isMobileVerified()){
            String accessToken = serviceManager.getTokenService().generateToken(userDetails);
            String refreshToken = serviceManager.getRefreshTokenService().createRefreshToken(userDetails);
            return responseBuilder.userToken(accessToken)
                    .refreshToken(refreshToken)
                    .defaultVendorId(1L)
                    //.activeSubscriptions(List.of())
                    .address(userDetails.getAddress())
                    .build();
        }else{
            serviceManager.getNotificationContext().sendOTPMessage(NotificationType.SMS,userDetails.getFullMobileNumber());
            return responseBuilder
                    .message("OTP sent to your registered mobile number.")
                    .build();
        }

    }
}
