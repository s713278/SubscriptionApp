package com.app.auth.services;

import com.app.payloads.request.MobileSignUpRequest;
import com.app.payloads.response.SignUpResponse;
import org.springframework.stereotype.Component;

@Component("socialSignUpStrategy")
public class SocialSignUpStrategy implements SignUpStrategy<MobileSignUpRequest> {
    @Override
    public SignUpResponse signUp(MobileSignUpRequest request) {
        // Email sign-up logic
        return null;
    }
}
