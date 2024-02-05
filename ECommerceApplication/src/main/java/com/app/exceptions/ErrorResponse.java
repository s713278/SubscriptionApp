package com.app.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    @JsonProperty("error_code")
    private String errorCode;
    
    @JsonProperty("user_message")
    private String userMessage;
    
    @JsonProperty("error_details")
    private String errorDetails;

}
