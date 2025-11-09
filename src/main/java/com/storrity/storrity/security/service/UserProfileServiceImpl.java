/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import com.storrity.storrity.security.dto.UserProfileDto;
import com.storrity.storrity.security.dto.UserProfileUpdateDto;
import com.storrity.storrity.security.entity.UserProfile;
import com.storrity.storrity.security.entity.UserProfileQueryParams;
import com.storrity.storrity.security.repository.UserProfileRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class UserProfileServiceImpl implements UserProfileService{
    
    private final UserProfileRepository userProfileRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, ApplicationEventPublisher publisher) {
        this.userProfileRepository = userProfileRepository;
        this.publisher = publisher;
    }

    @Override
    public UserProfileDto fetch(String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundAppException("User profile not found with username: " + username));
        
        return UserProfileDto.from(userProfile);
    }

    @Override
    public List<UserProfileDto> list(UserProfileQueryParams params) {
        List<UserProfile> userProfiles = userProfileRepository.list(params);
        return userProfiles.stream().map(UserProfileDto::from).collect(Collectors.toList());
    }

    @Override
    public CountDto count(UserProfileQueryParams params) {
        Long count = userProfileRepository.countRecords(params);
        return new CountDto(count);
    }

    @Transactional
    @Override
    public UserProfileDto update(String username, UserProfileUpdateDto dto) {
        UserProfile userProfile = userProfileRepository.findByUsernameForUpdate(username)
                .orElseThrow(() -> new ResourceNotFoundAppException("User profile not found with username: " + username));
        
        if(dto.getFullName() != null){
            userProfile.setFullName(dto.getFullName());
        }
        
        if(dto.getPhone()!= null){
            userProfile.setPhone(dto.getPhone());
        }
        
        if(dto.getEmail()!= null){
            userProfile.setEmail(dto.getEmail());
        }
        
        if(dto.getFullName() != null){
            userProfile.setFullName(dto.getFullName());
        }
        
        if(dto.getState()!= null){
            userProfile.setState(dto.getState());
        }
        
        if(dto.getCity()!= null){
            userProfile.setCity(dto.getCity());
        }
        
        if(dto.getStreet()!= null){
            userProfile.setStreet(dto.getStreet());
        }        
        
        UserProfile savedUserProfile = userProfileRepository.save(userProfile);
//        @Todo publish event here
        return UserProfileDto.from(savedUserProfile);
    }
}
