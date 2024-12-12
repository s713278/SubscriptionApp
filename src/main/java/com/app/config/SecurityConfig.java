package com.app.config;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.app.security.AppFilter;
import com.app.security.CustomAccessDeniedHandler;
import com.app.security.CustomAuthenticationEntryPoint;
import com.app.security.JWTFilter;
import com.app.services.constants.MDCConstants;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private AppFilter appFilter;

    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(UserDetailsService userDetailsService,
            CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF since we're using tokens (JWT, session cookies) in REST APIs
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfig->corsConfig.configurationSource(corsConfigurationSource()))
                // Allow stateless sessions or create sessions on login
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Authorization rules
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, AppConstants.PUBLIC_GET_URLS).permitAll()
                            .requestMatchers(HttpMethod.POST, AppConstants.PUBLIC_POST_URLS).permitAll()

                            // User-level access
                            .requestMatchers(HttpMethod.PATCH, AppConstants.USER_PATCH_URLS).hasAnyAuthority("USER")
                            .requestMatchers(HttpMethod.PUT, AppConstants.USER_PUT_URLS).hasAnyAuthority("USER")
                            .requestMatchers(HttpMethod.GET, AppConstants.USER_GET_URLS)
                            .hasAnyAuthority("USER", "ADMIN")

                            // User and Vendor level access
                            .requestMatchers(HttpMethod.PATCH, AppConstants.VENDOR_USER_SUB_PATCH_URLS).hasAnyAuthority("USER", "VENDOR")
                            .requestMatchers(HttpMethod.POST, AppConstants.VENDOR_USER_SUB_POST_URLS).hasAnyAuthority("USER", "VENDOR")
                            .requestMatchers(HttpMethod.DELETE, AppConstants.VENDOR_USER_SUB_DELETE_URLS).hasAnyAuthority("USER", "VENDOR")
                            .requestMatchers(HttpMethod.GET, AppConstants.VENDOR_USER_SUB_GET_URLS).hasAnyAuthority("USER", "VENDOR")

                            // Admin level access
                            .requestMatchers(HttpMethod.POST, AppConstants.VENDOR_URLS)
                            .hasAnyAuthority("VENDOR", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, AppConstants.VENDOR_URLS)
                            .hasAnyAuthority("VENDOR", "ADMIN")
                            .requestMatchers(HttpMethod.DELETE, AppConstants.VENDOR_URLS)
                            .hasAnyAuthority("VENDOR", "ADMIN")

                            .requestMatchers(HttpMethod.POST, AppConstants.ADMIN_URLS).hasAnyAuthority("ADMIN")
                            .requestMatchers(HttpMethod.PUT, AppConstants.ADMIN_URLS).hasAnyAuthority("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, AppConstants.ADMIN_URLS).hasAnyAuthority("ADMIN")
                            .anyRequest().authenticated();
                })
                // Use basic form-based authentication for the REST APIs
                // .formLogin(login ->
                // login.loginProcessingUrl(AppConstants.SIGN_IN_URL).permitAll() // URL to
                // submit login request
                // )
                // Logout configuration for REST API
                .logout(logout -> logout.logoutUrl(AppConstants.SIGN_OUT_URL).invalidateHttpSession(true)
                        .deleteCookies(AppConstants.JSESSION_ID)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("Signout out successfully");
                        }))
                .exceptionHandling(t -> t
                        .authenticationEntryPoint(authenticationEntryPoint/*
                                                                             * (request, response, authException) ->
                                                                             * response .sendError(HttpServletResponse.
                                                                             * SC_UNAUTHORIZED, "Unauthorized Access")
                                                                             */)
                        .accessDeniedHandler(accessDeniedHandler));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterAfter(appFilter, UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(daoAuthenticationProvider());
        DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();

        return defaultSecurityFilterChain;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(corsConfig.getAllowedOrigins());
        config.setAllowedMethods(corsConfig.getAllowedMethods());
        config.setAllowCredentials(corsConfig.isAllowCredentials());
        config.setMaxAge(Duration.ofMinutes(10));
        config.setAllowedHeaders(corsConfig.getAllowedHeaders());
        config.setExposedHeaders(corsConfig.getExposedHeaders());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
