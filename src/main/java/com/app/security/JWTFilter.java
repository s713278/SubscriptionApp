package com.app.security;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.config.AppConstants;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIErrorResponse;
import com.app.exceptions.APIException;
import com.app.services.auth.UserAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final TokenService jwtUtil;
    private final UserAuthService apiAuthValidator;
    private final ObjectMapper mapper;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(AppConstants.AUTHORIZATION_HEADER);
        String userId = "";

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt == null || jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                APIErrorResponse apiError = new APIErrorResponse();
                apiError = new APIErrorResponse(APIErrorCode.API_401, "Invalid JWT token in Bearer Header");
                String responseJson = mapper.writeValueAsString(apiError);
                response.getWriter().write(responseJson);
                return;  // Return here to stop further processing
            } else {
                try {
                    userId = jwtUtil.validateTokenAndRetrieveSubject(jwt);
                    
                    // Authenticate the user if present
                    apiAuthValidator.authenticateUser(Long.parseLong(userId))
                        .ifPresent(SecurityContextHolder.getContext()::setAuthentication);

                    // Proceed with the next filter after setting the context
                    filterChain.doFilter(request, response);
                    return;  // Return after filter chain processing

                } catch (Exception exception) {
                    log.error("Exception occurred while authenticating token for user {}", userId, exception);
                    extracted(response, exception);

                    // Stop further execution after writing the response
                    return;
                }
            }
        } else {
            // Proceed with the next filter if no auth header is present
            filterChain.doFilter(request, response);
        }
    }

    private void extracted(HttpServletResponse response, Exception exception)
            throws JsonProcessingException, IOException {
        // Send a proper 401 response with JSON body
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        APIErrorResponse apiError = new APIErrorResponse();
        if(exception instanceof APIException) {
            APIException apiException = (APIException)exception;
              apiError = new APIErrorResponse(apiException.getApiErrorCode(),apiException.getFailureReason());
        }else {
            apiError = new APIErrorResponse(APIErrorCode.API_401, exception.getMessage());
        }
        String responseJson = mapper.writeValueAsString(apiError);
        response.getWriter().write(responseJson);
    }

}
