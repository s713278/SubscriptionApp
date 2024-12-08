package com.app.services.auth.signup;

import com.app.config.GlobalConfig;
import com.app.constants.NotificationType;
import com.app.payloads.request.SignUpRequest;
import com.app.payloads.response.SignUpDTO;
import com.app.repositories.RepositoryManager;
import com.app.services.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class AbstractSignUpService<T extends SignUpRequest> {

    private static final Logger log = LoggerFactory.getLogger(AbstractSignUpService.class);
    @Autowired
    protected RepositoryManager repoManager;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected GlobalConfig globalConfig;

    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    @Autowired
    protected ServiceManager serviceManager;

    public final SignUpDTO processSignUp(T user) {
        preSignUpOperations(user);
       var response= doSignUp(user);
        postSignUpOperations(response);
        return response;
    }

    protected void preSignUpOperations(T user) {
    }

    protected abstract SignUpDTO doSignUp(T user);

    protected void postSignUpOperations(SignUpDTO signUpDTO) {
        serviceManager.getNotificationContext().sendOTPMessage(NotificationType.SMS,signUpDTO.mobile());
    }
}
