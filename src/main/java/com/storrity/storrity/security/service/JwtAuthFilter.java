/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author Seun Owa
 */
public class JwtAuthFilter extends GenericFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) request;
        String header = http.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.isValid(token)) {
                String username = jwtUtil.extractUsername(token);
                String clientId = jwtUtil.extractClientId(token);
                String clientName = jwtUtil.extractClientName(token);
                boolean accessAllStores = jwtUtil.extractAccessAllStores(token);
                Set<UUID> accessibleStoreIds = jwtUtil.extractAccessibleStoreIds(token);
                
                List<SimpleGrantedAuthority> authorities = jwtUtil
                        .extractAuthorities(token)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                
                AuthenticatedUser principal = new AuthenticatedUser(username, clientId, clientName,
                        accessAllStores, accessibleStoreIds);

                var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
