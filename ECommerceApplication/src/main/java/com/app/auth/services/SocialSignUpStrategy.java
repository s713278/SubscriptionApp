package com.app.auth.services;

import org.springframework.stereotype.Component;

import com.app.payloads.request.MobileSignUpRequest;
import com.app.payloads.response.SignUpDTO;

@Component("socialSignUpStrategy")
public class SocialSignUpStrategy implements SignUpStrategy<MobileSignUpRequest> {
    @Override
    public SignUpDTO signUp(MobileSignUpRequest request) {
        // Email sign-up logic
        return null;
    }
}
