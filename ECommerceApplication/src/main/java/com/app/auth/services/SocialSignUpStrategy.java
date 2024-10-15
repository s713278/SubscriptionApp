package com.app.auth.services;

import org.springframework.stereotype.Component;

import com.app.payloads.request.MobileSignUpRequest;
import com.app.payloads.response.SignUpResponse;

@Component("socialSignUpStrategy")
public class SocialSignUpStrategy implements SignUpStrategy<MobileSignUpRequest> {
    @Override
    public SignUpResponse signUp(MobileSignUpRequest request) {
        // Email sign-up logic
        return null;
    }
}
