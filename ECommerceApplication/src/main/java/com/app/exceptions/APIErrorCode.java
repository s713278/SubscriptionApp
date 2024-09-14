package com.app.exceptions;

import org.springframework.http.HttpStatus;

public enum APIErrorCode {

    API_404(HttpStatus.NOT_FOUND, "Please change the input and Try again."),
    API_400(HttpStatus.BAD_REQUEST, "Please check the request and try again."),
    API_302(HttpStatus.FOUND, "Resource already existed."),
    API_500(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    API_417(HttpStatus.EXPECTATION_FAILED, "User creation is failed.");

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
