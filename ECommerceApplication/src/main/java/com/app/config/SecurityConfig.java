package com.app.config;

import com.app.security.AppFilter;
import com.app.security.CustomAccessDeniedHandler;
import com.app.security.CustomAuthenticationEntryPoint;
import com.app.security.JWTFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private AppFilter appFilter;

    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint  authenticationEntryPoint;
    private final CustomAccessDeniedHandler  accessDeniedHandler;

    public SecurityConfig(UserDetailsService userDetailsService,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
             CustomAccessDeniedHandler  accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF since we're using tokens (JWT, session cookies) in REST APIs
                .csrf(csrf -> csrf.disable())
                .cors(cors ->cors.disable() )
                // Allow stateless sessions or create sessions on login
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
                )
                // Authorization rules
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, AppConstants.PUBLIC_GET_URLS).permitAll()
                            .requestMatchers(HttpMethod.POST, AppConstants.PUBLIC_POST_URLS).permitAll()

                            .requestMatchers(HttpMethod.PATCH, AppConstants.AUTH_PATCH_URLS)
                            .hasAnyAuthority("USER")
                            
                            .requestMatchers(HttpMethod.GET, AppConstants.USER_URLS)
                            .hasAnyAuthority("USER", "STORE", "ADMIN")

                            .requestMatchers(HttpMethod.POST, AppConstants.VENDOR_URLS)
                            .hasAnyAuthority("STORE", "ADMIN").requestMatchers(HttpMethod.PUT, AppConstants.VENDOR_URLS)
                            .hasAnyAuthority("STORE", "ADMIN")
                            .requestMatchers(HttpMethod.DELETE, AppConstants.VENDOR_URLS)
                            .hasAnyAuthority("STORE", "ADMIN")

                            .requestMatchers(HttpMethod.POST, AppConstants.ADMIN_URLS).hasAnyAuthority("ADMIN")
                            .requestMatchers(HttpMethod.PUT, AppConstants.ADMIN_URLS).hasAnyAuthority("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, AppConstants.ADMIN_URLS).hasAnyAuthority("ADMIN")
                            .anyRequest().authenticated();
                })
                // Use basic form-based authentication for the REST APIs
                //.formLogin(login -> login.loginProcessingUrl(AppConstants.SIGN_IN_URL).permitAll() // URL to submit login request
                //)
                // Logout configuration for REST API
                .logout(logout -> logout.logoutUrl(AppConstants.SIGN_OUT_URL).invalidateHttpSession(true)
                        .deleteCookies(AppConstants.JSESSION_ID)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("Signout out successfully");
                        }))
                .exceptionHandling(t -> t.authenticationEntryPoint(authenticationEntryPoint/*
                                                                     * (request, response, authException) -> response
                                                                     * .sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                                                     * "Unauthorized Access")
                                                                     */).accessDeniedHandler(accessDeniedHandler));

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
}
