/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.license.service;

import com.storrity.storrity.license.dto.LicenseDto;
import com.storrity.storrity.util.exception.BadRequestAppException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Seun Owa
 */
@Component
public class LicenseJwtUtil {
    
    private final String secret = "appstorritysecret9364appstorritysecret9364"; // ≥256-bit
    private final long expiration = 86400000; // 1 day
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());
    
    private static final String LICENSE_PATH =
            System.getProperty("user.home") + "/.storrity/lic/license.txt";

    public String generateToken(String owner) {   
        String identifier = MachineIdentifier.getMachineUUID();
        return Jwts.builder()
        .setSubject(owner)
        .claim("noOfClients", 5)
        .claim("systemIdentifier", identifier)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
    }
    
    public String fetchLicense() {
        Path path = Paths.get(LICENSE_PATH);

        if (!Files.exists(path)) {
            throw new IllegalStateException("License file not found at: " + LICENSE_PATH);
        }

        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read license file at: " + LICENSE_PATH, e);
        }
    }

    public String extractSubject(String token) {
        return getClaims(token).getSubject();
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
    
    public LicenseDto getInstalledLicenseDetails(){
        String token = fetchLicense();
        Claims claims = getClaims(token);
        return LicenseDto.builder()
                .expiration(claims.getExpiration())
                .issuedAt(claims.getIssuedAt())
                .noOfClients((Integer) claims.get("noOfClients"))
                .systemIdentifier((String)claims.get("systemIdentifier"))
                .build();
    }
    
    public LicenseDto importLicense(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestAppException("No license file uploaded.");
        }

        String token = null;
        try {
            // Read file content (license.txt contains only the JWT)
            token = new String(file.getBytes()).trim();
        } catch (IOException e) {
            throw new BadRequestAppException("License file is empty or invalid.");
        }        
        return importLicense(token);
    }
    
    public LicenseDto importLicense(String token){
        LicenseDto licenseDto = validateLicense(token);

        // Save the license
        Path path = Paths.get(LICENSE_PATH);
        try {
            Files.writeString(path, token, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            throw new BadRequestAppException("Error persisting file");
        }
        
        return licenseDto;
    }
    
    public LicenseDto validateLicense(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestAppException("No license file uploaded.");
        }

        String token = null;
        try {
            // Read file content (license.txt contains only the JWT)
            token = new String(file.getBytes()).trim();
        } catch (IOException e) {
            throw new BadRequestAppException("License file is empty or invalid.");
        }        
        return validateLicense(token);
    }
    
    public LicenseDto validateLicense(String token){
        if (!StringUtils.hasText(token)) {
            throw new BadRequestAppException("License provided is empty or invalid.");
         }

        // Parse and validate token
        Claims claims = getClaims(token);

        // Validate system identifier
        String expectedId = MachineIdentifier.getMachineUUID();
        String systemIdentifier = (String) claims.get("systemIdentifier");

        if (!expectedId.equals(systemIdentifier)) {
            throw new BadRequestAppException("License does not belong to this system.");
        }

        // Validate expiration
        Date expiry = claims.getExpiration();
        if (expiry.before(new Date())) {
            throw new BadRequestAppException("License has expired.");
        }
        
        return LicenseDto.builder()
                .expiration(expiry)
                .issuedAt(claims.getIssuedAt())
                .noOfClients((Integer) claims.get("noOfClients"))
                .systemIdentifier(systemIdentifier)
                .build();
    }
}
