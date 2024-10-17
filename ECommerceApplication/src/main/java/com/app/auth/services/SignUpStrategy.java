package com.app.auth.services;

import com.app.payloads.response.SignUpDTO;

public interface SignUpStrategy<T> {

    SignUpDTO signUp(T signUpRequest);

}
