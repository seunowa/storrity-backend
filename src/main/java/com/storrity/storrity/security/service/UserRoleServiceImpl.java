/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import com.storrity.storrity.security.dto.UserRoleCreationDto;
import com.storrity.storrity.security.dto.UserRoleDto;
import com.storrity.storrity.security.dto.UserRoleUpdateDto;
import com.storrity.storrity.security.entity.UserPermission;
import com.storrity.storrity.security.entity.UserRole;
import com.storrity.storrity.security.entity.UserRoleQueryParams;
import com.storrity.storrity.security.event.UserRoleCreatedEvent;
import com.storrity.storrity.security.event.UserRoleDeletedEvent;
import com.storrity.storrity.security.event.UserRoleUpdatedEvent;
import com.storrity.storrity.security.repository.UserPermissionRepository;
import com.storrity.storrity.security.repository.UserRepository;
import com.storrity.storrity.security.repository.UserRoleRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
public class UserRoleServiceImpl implements UserRoleService{
    
    private final UserRoleRepository userRoleRepository;
    private final UserPermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository,
            UserPermissionRepository permissionRepository,
            UserRepository userRepository,
            ApplicationEventPublisher publisher) {
        this.userRoleRepository = userRoleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public UserRoleDto create(UserRoleCreationDto dto) {
//        UserRole userRole = UserRole.builder()
//                .id(dto.getRole())
//                .permissions(dto.getPermissions())
//                .build();
//        UserRole savedUserRole = userRoleRepository.save(userRole);
//        publisher.publishEvent(new UserRoleCreatedEvent(savedUserRole));
//        return UserRoleDto.from(savedUserRole);
        
        String roleId = dto.getRole().trim().toUpperCase().replace(' ', '_');
        if (userRoleRepository.existsById(roleId)) throw new IllegalArgumentException("Role already exists: " + roleId);
        UserRole role = UserRole.builder().id(roleId)
                .permissions(resolvePermissions(dto.getPermissions())).build();
        UserRole saved = userRoleRepository.save(role); publisher.publishEvent(new UserRoleCreatedEvent(saved)); return UserRoleDto.from(saved);
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
    public UserRoleDto update(String id, UserRoleUpdateDto dto) {
//        UserRole r = fetchRaw(role);
//        
//        if(dto.getPermissions() != null){
//            r.getPermissions().clear(); // remove existing permissions
//            r.getPermissions().addAll(dto.getPermissions()); // add new ones
//        }
//        
//        UserRole savedUserRole = userRoleRepository.save(r);
//        
//        publisher.publishEvent(new UserRoleUpdatedEvent(savedUserRole, r));
//        return UserRoleDto.from(r);

        UserRole role = fetchRaw(id);
        if (role.isSystemRole() && "SUPER_ADMIN".equals(role.getId())){
            throw new IllegalArgumentException("SUPER_ADMIN permissions cannot be modified");
        }
        if (dto.getPermissions() != null){
            role.setPermissions(resolvePermissions(dto.getPermissions()));
        }
        UserRole saved = userRoleRepository.save(role);
        publisher.publishEvent(new UserRoleUpdatedEvent(saved, role));
        return UserRoleDto.from(saved);
    }

    @Override
    public UserRoleDto delete(String id) {
//        UserRole userRoles = fetchRaw(role);
//        userRoleRepository.delete(userRoles);        
//        publisher.publishEvent(new UserRoleDeletedEvent(userRoles));
//        return UserRoleDto.from(userRoles);
        
        UserRole role = fetchRaw(id);
        if (role.isSystemRole()){
            throw new IllegalArgumentException("Default system roles cannot be deleted: " + id);
        }
        if (userRepository.existsByRoles_Id(id)){
            throw new IllegalArgumentException("Cannot delete a role assigned to users");
        }
        userRoleRepository.delete(role);
        publisher.publishEvent(new UserRoleDeletedEvent(role));
        return UserRoleDto.from(role);
    }
    
    
    private Set<UserPermission> resolvePermissions(Set<String> ids) {
        if (ids == null || ids.isEmpty()) return new LinkedHashSet<>();
        Set<UserPermission> found = permissionRepository.findAllById(ids).stream().collect(Collectors.toCollection(LinkedHashSet::new));
        Set<String> foundIds = found.stream().map(UserPermission::getId).collect(Collectors.toSet());
        Set<String> missing = new LinkedHashSet<>(ids); missing.removeAll(foundIds);
        if (!missing.isEmpty()) throw new IllegalArgumentException("Unknown permission keys: " + missing);
        return found;
    }
}
