package com.app.auth.services;

import org.springframework.stereotype.Component;

import com.app.payloads.request.EmailSignUpRequest;
import com.app.payloads.response.SignUpDTO;

import lombok.RequiredArgsConstructor;

@Component("emailSignUpStrategy")
@RequiredArgsConstructor
public class EmailSignUpStrategy implements SignUpStrategy<EmailSignUpRequest> {
    
    private final EmailSignUpService emailSignUpService;
    @Override
    public SignUpDTO signUp(EmailSignUpRequest request) {
        return emailSignUpService.processSignUp(request);
    }

}
