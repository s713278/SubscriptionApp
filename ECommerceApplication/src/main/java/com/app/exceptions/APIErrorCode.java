package com.app.exceptions;

import org.springframework.http.HttpStatus;

public enum APIErrorCode {

    API_404(HttpStatus.NOT_FOUND, "No data found!!"),
    API_400(HttpStatus.BAD_REQUEST, "Please check the request and try again!!"),
    API_401(HttpStatus.UNAUTHORIZED, "Authorization failed!!"),
    API_302(HttpStatus.FOUND, "Resource already existed."),
    API_500(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    API_417(HttpStatus.EXPECTATION_FAILED, "User creation is failed."),
    API_418(HttpStatus.EXPECTATION_FAILED, "Customer subscription creation failed");

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
