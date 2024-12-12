package com.app.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum APIErrorCode {

    // General Errors
    API_404(HttpStatus.NOT_FOUND, "No data found!!"),
    API_405(HttpStatus.NOT_FOUND, "No registered vendors found in this area!!"),

    API_400(HttpStatus.BAD_REQUEST, "Please check the request and try again!!"),
    API_401(HttpStatus.UNAUTHORIZED, "User authentication failed!!"),
    API_403(HttpStatus.FORBIDDEN, "User authorization failed!!"),
    API_409(HttpStatus.CONFLICT, "Data already existed in system."),
    API_500(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong,Please try after sometime!!"),
    API_429(HttpStatus.TOO_MANY_REQUESTS, "Maximum attempts exceeded. Please try again later."),

    // Subscription Errors
    USER_SIGNUP_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "User signup validation failed."),
    USER_CREATION_FAILED(HttpStatus.EXPECTATION_FAILED, "Unable to create new signup."),
    DUPLICATE_REQUEST(HttpStatus.CONFLICT, "Resource is already existed."),

    // Subscription Errors
    SUBSCRIPTION_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Subscription validation failed."),
    SUBSCRIPTION_CREATION_FAILED(HttpStatus.EXPECTATION_FAILED, "Subscription creation is failed."),

    // Address Errors
    ADDRESS_CREATION_FAILED(HttpStatus.EXPECTATION_FAILED, "Unable to create a new address."),
    ADDRESS_UPDATE_FAILED(HttpStatus.EXPECTATION_FAILED, "Unable to update the address."),

    // OTP Errors
    OTP_VERIFICATION_FAILED(HttpStatus.EXPECTATION_FAILED, "Unable to verify OTP."),
    OTP_MAX_ATTEMPTS_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "Maximum OTP verification attempts exceeded. Please try again later."),

    INTERNAL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to process the request at this time due to technical issue,Please try after sometime.");

    private final HttpStatus httpStatus;
    private final String userMessage;

    APIErrorCode(HttpStatus httpStatus, String userMessage) {
        this.httpStatus = httpStatus;
        this.userMessage = userMessage;
    }

}
