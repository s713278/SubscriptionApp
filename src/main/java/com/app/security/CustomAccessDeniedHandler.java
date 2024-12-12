package com.app.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
         response.setStatus(HttpServletResponse.SC_FORBIDDEN);
         response.setContentType("application/json");
         APIErrorResponse apiError = new APIErrorResponse();
         apiError = new APIErrorResponse(APIErrorCode.API_403,accessDeniedException.getMessage());
         String responseJson = objectMapper.writeValueAsString(apiError);
         response.getWriter().write(responseJson);
    }
}
