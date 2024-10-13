package com.app.exceptions;

import org.springframework.http.HttpStatus;

public enum APIErrorCode {

    API_404(HttpStatus.NOT_FOUND, "No data found!!"),
    API_400(HttpStatus.BAD_REQUEST, "Please check the request and try again!!"),
    API_401(HttpStatus.UNAUTHORIZED, "User authentication failed!!"),
    API_403(HttpStatus.UNAUTHORIZED, "User authorization failed!!"),
    API_409(HttpStatus.CONFLICT, "Resource already existed."),
    API_500(HttpStatus.INTERNAL_SERVER_ERROR, "Technical issue occured,Please try after sometime!!"),
    API_417(HttpStatus.EXPECTATION_FAILED, "Unable to create new signup."),
    API_418(HttpStatus.EXPECTATION_FAILED, "Customer subscription creation failed"),
    API_419(HttpStatus.EXPECTATION_FAILED, "Unable to process the request at this time,Please try after sometime."),
    API_420(HttpStatus.EXPECTATION_FAILED, "Unable to verify OTP."),
    API_421(HttpStatus.EXPECTATION_FAILED, "Unable to update address");

    private final HttpStatus httpStatus;
    private final String userMessage;

    APIErrorCode(HttpStatus httpStatus, String userMessage) {
        this.httpStatus = httpStatus;
        this.userMessage = userMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
