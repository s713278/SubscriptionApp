package com.app.payloads.response;

import java.time.Instant;
import lombok.Data;

@Data
public class ApiResponse<T> {

    private Boolean success;
    private T data;
    //private Map<String, Object> metadata;
    private String userMessage; // Optional field for additional messages or errors
    private String apiMessage;
    private Instant timestamp;

    // Constructors, getters, setters, etc.

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(Boolean.TRUE);
        response.setData(data);
        response.setTimestamp(Instant.now());
        return response;
    }

    public static ApiResponse<?> error(String apiMessage,String userMessage) {
        ApiResponse<?> response = new ApiResponse<>();
        response.setSuccess(Boolean.FALSE);
        response.setApiMessage(apiMessage);
        response.setUserMessage(userMessage);
        response.setTimestamp(Instant.now());
        return response;
    }

    // Additional methods for handling metadata, adding links, etc.
}
