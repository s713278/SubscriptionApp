package com.app.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIException extends RuntimeException {
    
    private APIErrorCode errorCode;
    
    private String errorReason;

    private static final long serialVersionUID = 1L;

    public APIException() {}

    public APIException(String message) {
        super(message);
    }
  
    public APIException(APIErrorCode errorCode, String errorReason) {
        this.errorCode=errorCode;
        this.errorReason=errorReason;
    }
    
}
