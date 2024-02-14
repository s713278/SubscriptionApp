package com.app.security.filters;

import com.app.services.constants.MDCConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

public class MDCFilter extends OncePerRequestFilter{

    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestId = request.getHeader(MDCConstants.REQ_ID_KEY);
        try {
              if(requestId == null) {
                  requestId = UUID.randomUUID().toString();
              }
              MDC.put(MDCConstants.REQ_ID_KEY, requestId);
              
              if(!response.containsHeader(MDCConstants.REQ_ID_KEY)) {
                  response.addHeader(MDCConstants.REQ_HEADER_KEY, requestId);
              }
        }finally {
            MDC.remove(MDCConstants.REQ_ID_KEY);
        }
      
    }

}
