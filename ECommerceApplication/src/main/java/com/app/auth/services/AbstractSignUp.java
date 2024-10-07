package com.app.auth.services;

import com.app.config.GlobalConfig;
import com.app.payloads.response.SignUpResponse;
import com.app.repositories.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class AbstractSignUp<T> {

    @Autowired
    protected RepositoryManager repoManager;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected GlobalConfig globalConfig;

    @Autowired
    protected ApplicationEventPublisher eventPublisher;
    
    @Autowired
    protected OTPService otpService;

    public final SignUpResponse processSignUp(T user) {
        preSignUpOperations(user);
       var response= doSignUp(user);
        postSignUpOperations(user);
        return response;
    }

    protected void preSignUpOperations(T user) {
    }

    protected abstract SignUpResponse doSignUp(T user);

    protected void postSignUpOperations(T user) {
        // Common post-sign-up operations like sending notifications
    }
}
