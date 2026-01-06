/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.license.service;

import com.storrity.storrity.license.dto.IsAddClientSystemAllowedDto;
import com.storrity.storrity.license.dto.IsClientSystemLicensedDto;
import com.storrity.storrity.license.dto.LicenseDto;
import com.storrity.storrity.license.entity.ClientSystem;
import com.storrity.storrity.license.entity.ClientSystemStatus;
import com.storrity.storrity.license.repository.ClientSystemQueryParams;
import com.storrity.storrity.license.repository.ClientSystemRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.BadRequestAppException;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Seun Owa
 */
@Service
public class LicenseServiceImpl implements LicenseService{
    
    private final LicenseJwtUtil licenseJwtUtil;
    private final ClientSystemRepository clientSystemRepository;

    @Autowired
    public LicenseServiceImpl(LicenseJwtUtil licenseJwtUtil, ClientSystemRepository clientSystemRepository) {
        this.licenseJwtUtil = licenseJwtUtil;
        this.clientSystemRepository = clientSystemRepository;
    }
    
    @Override
    public String generateToken(String owner){
        return licenseJwtUtil.generateToken(owner);
    }

    @Override
    public String fetchLicense() {
        return licenseJwtUtil.fetchLicense();
    }

    @Override
    public LicenseDto getInstalledLicenseDetails() {
        return licenseJwtUtil.getInstalledLicenseDetails();
    }

    @Override
    public LicenseDto importLicense(MultipartFile file) {
        return licenseJwtUtil.importLicense(file);
    }

    @Override
    public LicenseDto importLicense(String token) {
        return licenseJwtUtil.importLicense(token);
    }

    @Override
    public LicenseDto validateLicense(MultipartFile file) {
        return licenseJwtUtil.validateLicense(file);
    }

    @Override
    public LicenseDto validateLicense(String token) {
        return licenseJwtUtil.validateLicense(token);
    }

    @Override
    public String getSystemIdentifier() {
        return MachineIdentifier.getMachineUUID();
    }

    @Override
    public IsClientSystemLicensedDto  isClientSystemLicensed(String clientId) {
        try{
            
            ClientSystem cs = clientSystemRepository.findByClientId(clientId)
                .orElseThrow(()-> new ResourceNotFoundAppException("Client system not found with clientId: " + clientId));
            
            if(ClientSystemStatus.INACTIVE.equals(cs.getStatus())){
                throw new BadRequestAppException("Client system is not licensed");
            }
            
            String token = fetchLicense();
            validateLicense(token);
        
            return IsClientSystemLicensedDto
                    .builder()
                    .isClientSystemLicensed(true)
                    .build();            
            
        }catch(ResourceNotFoundAppException e){
            throw new BadRequestAppException("Client system is not licensed");
        }        
    }

    @Override
    public IsAddClientSystemAllowedDto isAddClientSystemAllowed() {
        Long count = clientSystemRepository.countRecords(new ClientSystemQueryParams());
        
        String token = fetchLicense();
        LicenseDto licenseDto = validateLicense(token);
        Boolean isLicensed = licenseDto.getNoOfClients() > count;        
        
        return IsAddClientSystemAllowedDto
                .builder()
                .IsAddClientSystemAllowed(isLicensed)
                .build();
    }
    
}
