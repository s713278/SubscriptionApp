package com.app.exceptions;

import jakarta.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        APIErrorResponse apiError = new APIErrorResponse(APIErrorCode.API_500, ex.getMessage(), List.of(request.getDescription(false)));
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        APIErrorResponse apiError = new APIErrorResponse(APIErrorCode.API_404, ex.getMessage(), List.of(request.getDescription(false)));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());
        APIErrorResponse apiError = new APIErrorResponse(APIErrorCode.API_400, ex.getMessage(), validationErrors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIErrorResponse> handleEnumConversionError(HttpMessageNotReadableException ex, WebRequest request) {
        APIErrorResponse apiError = new APIErrorResponse(APIErrorCode.API_400, ex.getMessage(), List.of(request.getDescription(false)));
        return new ResponseEntity<>(apiError, APIErrorCode.API_400.getHttpStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        APIErrorResponse apiError = new APIErrorResponse(APIErrorCode.API_401, ex.getMessage());
        return new ResponseEntity<>(apiError, APIErrorCode.API_401.getHttpStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        APIErrorResponse apiError = new APIErrorResponse(APIErrorCode.API_400, ex.getMessage(), errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIErrorResponse> handleRuntimeException(RuntimeException e,WebRequest request) {
        APIErrorResponse apiError = new APIErrorResponse(APIErrorCode.API_500, e.getMessage(), List.of(request.getDescription(false)));
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIErrorResponse> handleAPIErrorResponse(APIErrorResponse apiError) {
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIErrorResponse> myConstraintsVoilationException(ConstraintViolationException e) {
        var errors = e.getConstraintViolations().stream().map(v -> v.getPropertyPath() + "==>" + v.getMessage())
                .collect(Collectors.toList());
        APIErrorResponse apiError = new APIErrorResponse(APIErrorCode.API_400, e.getMessage(), errors);
        return new ResponseEntity<>(apiError, APIErrorCode.API_400.getHttpStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<APIErrorResponse> myAuthenticationException(AuthenticationException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        APIErrorResponse apiError = new APIErrorResponse(APIErrorCode.API_401, ex.getMessage(), errors);
        return new ResponseEntity<>(apiError, APIErrorCode.API_401.getHttpStatus());
    }

}
