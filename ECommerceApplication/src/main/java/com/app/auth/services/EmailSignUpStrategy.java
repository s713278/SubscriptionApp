package com.app.auth.services;

import com.app.payloads.request.EmailSignUpRequest;
import com.app.payloads.response.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("emailSignUpStrategy")
@RequiredArgsConstructor
public class EmailSignUpStrategy implements SignUpStrategy<EmailSignUpRequest> {
    
    private final EmailSignUpService emailSignUpService;
    @Override
    public SignUpResponse signUp(EmailSignUpRequest request) {
        return emailSignUpService.processSignUp(request);
    }

}
