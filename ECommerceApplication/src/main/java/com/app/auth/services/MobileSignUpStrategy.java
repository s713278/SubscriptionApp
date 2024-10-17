package com.app.auth.services;

import org.springframework.stereotype.Component;

import com.app.payloads.request.MobileSignUpRequest;
import com.app.payloads.response.SignUpDTO;

import lombok.RequiredArgsConstructor;

@Component("mobileSignUpStrategy")
@RequiredArgsConstructor
public class MobileSignUpStrategy implements SignUpStrategy<MobileSignUpRequest> {

    private final MobileSignUpService mobileSignUpService;

    @Override
    public SignUpDTO signUp(MobileSignUpRequest request) {
        // Email sign-up logic
        return mobileSignUpService.processSignUp(request);
    }
}
