package com.app.exceptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.payloads.response.AppResponse;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        AppResponse<?> response = AppResponse.error(HttpStatus.BAD_REQUEST.value(), "User registration failed.", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AppResponse> handleRuntimeException(RuntimeException e) {
        List<String> errors = Collections.singletonList(e.getMessage());
        AppResponse<?> apiResponse = AppResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Syste Error",
                errors);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AppResponse> myResourceNotFoundException(ResourceNotFoundException e) {
        List<String> errors = Collections.singletonList(e.getMessage());
        AppResponse<?> apiResponse = AppResponse.error(HttpStatus.NOT_FOUND.value(), "Resournce Not Found.", errors);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<AppResponse> myAPIException(APIException e) {
        List<String> errors = Collections.singletonList(e.getMessage());
        AppResponse<?> errorResponse = AppResponse.error(e.getErrorCode().getHttpStatus().value(), e.getErrorReason(),
                errors);
        return new ResponseEntity<>(errorResponse, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> res = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();

            res.put(fieldName, message);
        });

        return new ResponseEntity<Map<String, String>>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> myConstraintsVoilationException(ConstraintViolationException e) {
        Map<String, String> res = new HashMap<>();

        e.getConstraintViolations().forEach(voilation -> {
            String fieldName = voilation.getPropertyPath().toString();
            String message = voilation.getMessage();

            res.put(fieldName, message);
        });
        return new ResponseEntity<Map<String, String>>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> myAuthenticationException(AuthenticationException e) {
        String res = e.getMessage();
        return new ResponseEntity<String>(res, HttpStatus.FORBIDDEN);
    }

}
