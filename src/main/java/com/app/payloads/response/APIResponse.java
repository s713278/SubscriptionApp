package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(value = Include.NON_NULL)
public class APIResponse<T> {

    @JsonProperty("success")
    private Boolean success;

    @Schema(description = "HTTP Status Code", example = "201")
    @JsonProperty("status")
    private int statusCode;

    @Schema(description = "Data")
    @JsonProperty("data")
    private T data;

    @Schema(description = "Response message", example = "Customer registered successfully")
    private String message; // Optional field for additional messages or errors
    private LocalDateTime timestamp;

//    @Schema(description = "Error messages (if any)")
  //  private List<String> errors;

    // Constructors, getters, setters, etc.

    public static <T> APIResponse<T> success(int statusCode, T data) {
        APIResponse<T> response = new APIResponse<>();
        response.setSuccess(Boolean.TRUE);
        response.setStatusCode(statusCode);
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
    
    public static <T> APIResponse<T> success(T data) {
        APIResponse<T> response = new APIResponse<>();
        response.setSuccess(Boolean.TRUE);
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        response.setStatusCode(HttpStatus.OK.value());
        return response;
    }

    public static APIResponse<?> error11(int statusCode, String message, List<String> errors) {
        APIResponse<?> response = new APIResponse<>();
        response.setSuccess(Boolean.FALSE);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
      //  response.setErrors(errors);
        response.setStatusCode(statusCode);
        return response;
    }
}
