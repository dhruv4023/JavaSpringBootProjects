package com.authserver.authserver.base.config;

import java.util.List;

import org.springframework.session.Session;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.authserver.authserver.base.ai.AiServiceAuthFilter;
import com.authserver.authserver.user.security.AuthUserDetailsService;
import com.authserver.authserver.user.security.DynamicAuthorizationFilter;
import com.authserver.authserver.user.security.JwtFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig<S extends Session> {

    private final AiServiceAuthFilter aiServiceAuthFilter;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private DynamicAuthorizationFilter dynamicAuthorizationFilter;

    SecurityConfig(AiServiceAuthFilter aiServiceAuthFilter) {
        this.aiServiceAuthFilter = aiServiceAuthFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AuthUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/signup", "/auth/login", "/auth/forgot-password", "/", "/ai/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                        .maximumSessions(1)
                        .sessionRegistry(sessionRegistry()))
                .addFilterBefore((request, response, chain) -> {
                    HttpServletRequest httpRequest = (HttpServletRequest) request;
                    System.out.println(
                            "Requested URI: " + httpRequest.getRequestURI() + " Method: " + httpRequest.getMethod());
                    chain.doFilter(request, response);
                }, UsernamePasswordAuthenticationFilter.class)
                // JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(aiServiceAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(dynamicAuthorizationFilter, JwtFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }

    @Autowired
    private FindByIndexNameSessionRepository<S> sessionRepository;

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Time-Zone", "X-AI-Service-Key",
                "X-AI-Signature", "X-Request-Time"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
