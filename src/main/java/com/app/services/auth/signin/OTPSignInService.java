package com.app.services.auth.signin;

import com.app.entites.Customer;
import com.app.payloads.request.OTPVerificationRequest;
import com.app.payloads.request.SignInRequest;
import com.app.payloads.response.AuthDetailsDTO;
import com.app.services.auth.dto.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPSignInService extends AbstractSignInService{


    @Override
    protected AuthDetailsDTO doSignIn(SignInRequest signInRequest,Customer user) {
        OTPVerificationRequest request=new OTPVerificationRequest();
        request.setCountryCode(signInRequest.getCountryCode());
        request.setMobile(signInRequest.getMobile());
        request.setOtp(signInRequest.getPassword());
        // Find the user by mobile number
        serviceManager.getOtpService().verifyOtp(user.getFullMobileNumber(),request.getOtp());
        // Mark user as verified
        var userDetails= Optional.of(user).map(AuthUserDetails::new).get();
        userDetails.setMobileVerified(true);
        UsernamePasswordAuthenticationToken authentication=
                new UsernamePasswordAuthenticationToken(userDetails,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return getSignInResponse(userDetails);
    }

    @Override
    protected void postSignIn(AuthDetailsDTO authDetailsDTO) {
        super.postSignIn(authDetailsDTO);
        serviceManager.getUserService().updateMobileVerifiedStatus(authDetailsDTO.getUserId(),true);
    }

}
