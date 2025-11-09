/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import java.util.Arrays;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *
 * @author Seun Owa
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
//                .csrf(csrf -> csrf
//                    .ignoringRequestMatchers("/h2-console/**") // allow CSRF for H2
//                )
//                .headers(headers -> headers
//                    .frameOptions().disable() // required for H2 to work in iframe
//                )
                .csrf(csrf -> csrf.disable()) // disable CSRF globally for stateless JWT auth
                .headers(headers -> headers
                    .frameOptions(frame -> frame.disable()) // allow H2 console in iframe
                )
                
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // enable Spring Security CORS support
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/api/v1/auth/**",
                            "/api/v1/ping/**",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/v3/api-docs.yaml",
                            "/swagger-resources/**",
                            "/webjars/**",
                            "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOriginPatterns(Arrays.asList("*"));
            configuration.addAllowedHeader("*");
            configuration.setAllowedMethods(Arrays.asList("*"));
            configuration.setAllowCredentials(true);
            
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
    }
}
