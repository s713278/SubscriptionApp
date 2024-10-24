package com.app.exceptions;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIErrorResponse {
    
    private boolean success;
    @JsonIgnore
    private APIErrorCode errorCode;
    private int status;
    @JsonProperty("failure_reason")
    private String failureReason;
    @JsonProperty("user_message")
    private String userMessage;
    private LocalDateTime timestamp;
    @JsonProperty("api_details")
    private List<String> details;

    public APIErrorResponse() {
    }

    public APIErrorResponse(APIErrorCode apiErrorCode, String failureReason) {
        this.status = apiErrorCode.getHttpStatus().value();
        this.success= false;
        this.failureReason = failureReason;
        this.userMessage = apiErrorCode.getUserMessage();
        this.details = List.of();
        this.timestamp = LocalDateTime.now();
    }

    public APIErrorResponse(APIErrorCode apiErrorCode, String failureReason, List<String> apiDetails) {
        this.status = apiErrorCode.getHttpStatus().value();
        this.success= false;
        this.failureReason = failureReason;
        this.userMessage = apiErrorCode.getUserMessage();
        this.details = apiDetails;
        this.timestamp = LocalDateTime.now();
    }
}
