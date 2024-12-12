package com.app.security.filters;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.services.constants.MDCConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestId = request.getHeader(MDCConstants.REQ_ID_KEY);
        try {
            if (!StringUtils.hasText(requestId)) {
                requestId = UUID.randomUUID().toString();
            }
            MDC.put(MDCConstants.REQ_ID_KEY, requestId);

            if (!response.containsHeader(MDCConstants.REQ_ID_KEY)) {
                response.addHeader(MDCConstants.REQ_ID_KEY, requestId);
            }

            // Proceed with the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Clean up MDC after the request is completed
            MDC.remove(MDCConstants.REQ_ID_KEY);
        }
    }
}
