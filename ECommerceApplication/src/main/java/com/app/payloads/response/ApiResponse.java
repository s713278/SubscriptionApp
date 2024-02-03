package com.app.payloads.response;

import java.util.Map;
import lombok.Data;

@Data
public class ApiResponse<T> {

  private Boolean success;
  private T data;
  private Map<String, Object> metadata;
  private String message; // Optional field for additional messages or errors

  // Constructors, getters, setters, etc.

  public static <T> ApiResponse<T> success(T data) {
    ApiResponse<T> response = new ApiResponse<>();
    response.setSuccess(Boolean.TRUE);
    response.setData(data);
    return response;
  }

  public static ApiResponse<?> error(String message) {
    ApiResponse<?> response = new ApiResponse<>();
    response.setSuccess(Boolean.TRUE);
    response.setMessage(message);
    return response;
  }

  // Additional methods for handling metadata, adding links, etc.
}
