/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.license.service;

import com.storrity.storrity.license.dto.IsAddClientSystemAllowedDto;
import com.storrity.storrity.license.dto.IsClientSystemLicensedDto;
import com.storrity.storrity.license.dto.LicenseDto;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Seun Owa
 */
public interface LicenseService {
    @Deprecated
    public String generateToken(String owner);
    public String fetchLicense();
    public LicenseDto getInstalledLicenseDetails();
    public LicenseDto importLicense(MultipartFile file);
    public LicenseDto importLicense(String token);
    public LicenseDto validateLicense(MultipartFile file);
    public LicenseDto validateLicense(String token);
    
    public String getSystemIdentifier();
    
    public  IsClientSystemLicensedDto isClientSystemLicensed(String clientId);
    public IsAddClientSystemAllowedDto  isAddClientSystemAllowed();
}
