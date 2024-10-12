package com.app.exceptions;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIException extends RuntimeException {

    private APIErrorCode apiErrorCode;

    private static final long serialVersionUID = 1L;

    private int status;
    private String failureReason;
    private String userMessage;
    private List<String> details;
    public APIException() {
    }

    public APIException(APIErrorCode apiErrorCode, String failureReason) {
        super(apiErrorCode.getUserMessage());
        this.apiErrorCode = apiErrorCode;
        this.status = apiErrorCode.getHttpStatus().value();
        this.failureReason = failureReason;
        this.userMessage = apiErrorCode.getUserMessage();
        this.details = List.of();
    }

    public APIException(APIErrorCode apiErrorCode, String failureReason, List<String> apiDetails) {
        super(apiErrorCode.getUserMessage());
        this.apiErrorCode = apiErrorCode;
        this.status = apiErrorCode.getHttpStatus().value();
        this.failureReason = failureReason;
        this.userMessage = apiErrorCode.getUserMessage();
        this.details = apiDetails;
    }
}
