/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.license.dto;

import io.jsonwebtoken.Claims;
import java.time.Instant;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class LicenseDto {
    private Integer noOfClients;
    private String systemIdentifier;
    private Date issuedAt;
    private Date expiration;
    private Boolean isExpired;
    
    public static LicenseDto from(Claims claims){
        Date expiry = claims.getExpiration();        
        return LicenseDto.builder()
                .expiration(expiry)
                .issuedAt(claims.getIssuedAt())
                .noOfClients((Integer) claims.get("noOfClients"))
                .systemIdentifier((String)claims.get("systemIdentifier"))
                .isExpired(Instant.now().isAfter(claims.getExpiration().toInstant()))
                .build();
    }
}
