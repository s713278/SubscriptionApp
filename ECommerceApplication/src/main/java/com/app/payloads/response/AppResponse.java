package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class AppResponse<T> {

    private Boolean success;

    @Schema(description = "HTTP Status Code", example = "201")
    @JsonProperty("status_code")
    private int statusCode;

    @Schema(description = "Additional data")
    private T data;

    @Schema(description = "Response message", example = "Customer registered successfully")
    private String message; // Optional field for additional messages or errors
    private LocalDateTime timestamp;

    @Schema(description = "Error messages (if any)")
    private List<String> errors;

    // Constructors, getters, setters, etc.

    public static <T> AppResponse<T> success(int statusCode, T data) {
        AppResponse<T> response = new AppResponse<>();
        response.setSuccess(Boolean.TRUE);
        response.setStatusCode(statusCode);
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    public static AppResponse<?> error(int statusCode, String message, List<String> errors) {
        AppResponse<?> response = new AppResponse<>();
        response.setSuccess(Boolean.FALSE);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        response.setErrors(errors);
        response.setStatusCode(statusCode);
        return response;
    }
}
