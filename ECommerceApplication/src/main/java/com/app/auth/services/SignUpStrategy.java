package com.app.auth.services;

import com.app.payloads.response.SignUpResponse;

public interface SignUpStrategy<T> {

    SignUpResponse signUp(T signUpRequest);

}
