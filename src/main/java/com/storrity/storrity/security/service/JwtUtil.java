/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import com.storrity.storrity.storeaccess.service.StoreAccessContext;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Seun Owa
 */
@Component
public class JwtUtil {
    private final String secret = "supersecurekeysupersecurekeysupersecurekey"; // ≥256-bit
    private final long expiration = 86400000; // 1 day
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    public String generateToken(String username, String clientId,
            String clientName, List<String> authorities,
            StoreAccessContext storeAccessContext) {
        
        StoreAccessContext context = storeAccessContext == null
                ? new StoreAccessContext(false, Set.of())
                : storeAccessContext;
        
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .claim("clientId", clientId)
                .claim("clientName", clientName)
                .claim("accessAllStores", context.accessAllStores())
                .claim("accessibleStoreIds", context.accessibleStoreIds().stream()
                        .map(UUID::toString)
                        .sorted()
                        .toList())
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
    
    public String extractClientName(String token){
        return getClaims(token).get("clientName", String.class);
    }

    public List<String> extractAuthorities(String token) {
        return getClaims(token).get("authorities", List.class);
    }

    public boolean extractAccessAllStores(String token) {
        Boolean claim = getClaims(token).get("accessAllStores", Boolean.class);
        return Boolean.TRUE.equals(claim);
    }

    public Set<UUID> extractAccessibleStoreIds(String token) {
        Object claim = getClaims(token).get("accessibleStoreIds");
        if (!(claim instanceof List<?> values)) {
            return Set.of();
        }
        return values.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(UUID::fromString)
                .collect(Collectors.toUnmodifiableSet());
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
