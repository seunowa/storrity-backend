/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import com.storrity.storrity.security.dto.UserRoleCreationDto;
import com.storrity.storrity.security.dto.UserRoleDto;
import com.storrity.storrity.security.dto.UserRoleUpdateDto;
import com.storrity.storrity.security.entity.UserRole;
import com.storrity.storrity.security.entity.UserRoleQueryParams;
import com.storrity.storrity.security.event.UserRoleCreatedEvent;
import com.storrity.storrity.security.event.UserRoleDeletedEvent;
import com.storrity.storrity.security.event.UserRoleUpdatedEvent;
import com.storrity.storrity.security.repository.UserRoleRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class UserRoleServiceImpl implements UserRoleService{
    
    private final UserRoleRepository userRoleRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, ApplicationEventPublisher publisher) {
        this.userRoleRepository = userRoleRepository;
        this.publisher = publisher;
    }

    @Override
    public UserRoleDto create(UserRoleCreationDto dto) {
        UserRole userRole = UserRole.builder()
                .id(dto.getRole())
                .permissions(dto.getPermissions())
                .build();
        UserRole savedUserRole = userRoleRepository.save(userRole);
        publisher.publishEvent(new UserRoleCreatedEvent(savedUserRole));
        return UserRoleDto.from(savedUserRole);
    }
    
    @Override
    public UserRole fetchRaw(String role) {
        UserRole userRole = userRoleRepository.findById(role)
                .orElseThrow(() -> new ResourceNotFoundAppException("Role not found with id: " + role));
        return userRole;
    }

    @Override
    public UserRoleDto fetch(String role) {
        UserRole userRole = fetchRaw(role);
        return UserRoleDto.from(userRole);
    }

    @Override
    public List<UserRoleDto> list(UserRoleQueryParams params) {
        List<UserRole> userRoles = userRoleRepository.list(params);
        return userRoles.stream().map(UserRoleDto::from).collect(Collectors.toList());
    }

    @Override
    public CountDto count(UserRoleQueryParams params) {
        Long count = userRoleRepository.countRecords(params);
        return new CountDto(count);
    }

    @Override
    public UserRoleDto update(String role, UserRoleUpdateDto dto) {
        UserRole r = fetchRaw(role);
        
        if(dto.getPermissions() != null){
            r.getPermissions().clear(); // remove existing permissions
            r.getPermissions().addAll(dto.getPermissions()); // add new ones
        }
        
        UserRole savedUserRole = userRoleRepository.save(r);
        
        publisher.publishEvent(new UserRoleUpdatedEvent(savedUserRole, r));
        return UserRoleDto.from(r);
    }

    @Override
    public UserRoleDto delete(String role) {
        UserRole userRoles = fetchRaw(role);
        userRoleRepository.delete(userRoles);        
        publisher.publishEvent(new UserRoleDeletedEvent(userRoles));
        return UserRoleDto.from(userRoles);
    }
    
}
