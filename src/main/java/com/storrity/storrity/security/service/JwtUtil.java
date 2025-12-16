/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;

/**
 *
 * @author Seun Owa
 */
@Component
public class JwtUtil {
    private final String secret = "supersecurekeysupersecurekeysupersecurekey"; // ≥256-bit
    private final long expiration = 86400000; // 1 day
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    public String generateToken(String username, String clientId, List<String> authorities) {
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .claim("clientId", clientId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }
    
    public String extractClientId(String token){
        return getClaims(token).get("clientId", String.class);
    }

    public List<String> extractAuthorities(String token) {
        return getClaims(token).get("authorities", List.class);
    }

    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }
}
