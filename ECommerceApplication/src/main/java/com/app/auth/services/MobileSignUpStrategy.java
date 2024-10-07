package com.app.auth.services;

import com.app.payloads.request.MobileSignUpRequest;
import com.app.payloads.response.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mobileSignUpStrategy")
@RequiredArgsConstructor
public class MobileSignUpStrategy implements SignUpStrategy<MobileSignUpRequest> {

    private final MobileSignUpService mobileSignUpService;

    @Override
    public SignUpResponse signUp(MobileSignUpRequest request) {
        // Email sign-up logic
        return mobileSignUpService.processSignUp(request);
    }
}
