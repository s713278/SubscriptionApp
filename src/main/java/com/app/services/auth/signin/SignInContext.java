package com.app.services.auth.signin;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.app.constants.SignInType;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.SignInRequest;
import com.app.payloads.response.AuthDetailsDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class SignInContext {

    private final PasswordSignInService passwordSignInService;
    private final OTPSignInService otpSignInService;
    public AuthDetailsDTO processSignIn(SignInType type,SignInRequest signInRequest){
        switch (type){
            case WITH_PASSWORD -> {
                return passwordSignInService.processSignIn(signInRequest);
            }
            case WITH_OTP -> {
                return  otpSignInService.processSignIn(signInRequest);
            }case null, default -> {
                throw new APIException(APIErrorCode.API_400,"Invalid sign in option,SignIn option should be one of the "+
                        Arrays.toString(SignInType.values()))       ;
            }
        }
    }
}
