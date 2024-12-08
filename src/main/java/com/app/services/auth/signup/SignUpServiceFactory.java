package com.app.services.auth.signup;

import com.app.payloads.request.SignUpRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Component
public class SignUpServiceFactory {
    private final Map<String, AbstractSignUpService<? extends SignUpRequest>> map ;

    public SignUpServiceFactory(Map<String, AbstractSignUpService<? extends SignUpRequest>> map){
        this.map=map;
    }

    @SuppressWarnings("unchecked")
    public <T extends SignUpRequest> AbstractSignUpService<T> get(String type) {
        return (AbstractSignUpService<T>) this.map.get(type);
    }
}