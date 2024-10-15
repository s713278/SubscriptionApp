package com.app.auth.services;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SignUpStrategyFactory {

    private final Map<String, SignUpStrategy> strategies;

    public SignUpStrategyFactory(Map<String, SignUpStrategy> strategies) {
        this.strategies = strategies;
    }
    public SignUpStrategy getStrategy(String signUpType) {
        SignUpStrategy strategy = strategies.get(signUpType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for: " + signUpType);
        }
        return strategy;
    }
    
    public Map<String, SignUpStrategy> getSignUpStrategies(){
        return this.strategies;
    }
}
