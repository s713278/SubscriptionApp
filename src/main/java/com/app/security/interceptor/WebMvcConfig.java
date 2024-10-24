package com.app.security.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInterceptor()).addPathPatterns("/api/**") // Add URL patterns to apply the
                                                                                    // interceptor to
                .excludePathPatterns("/api/public/**"); // Exclude URL patterns from interceptor
    }
}
