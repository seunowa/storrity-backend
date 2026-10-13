/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import com.storrity.storrity.security.dto.PasswordChangeDto;
import com.storrity.storrity.security.dto.PasswordRestDto;
import com.storrity.storrity.security.dto.RoleChangeDto;
import com.storrity.storrity.security.dto.UserCreationDto;
import com.storrity.storrity.security.dto.UserDetailDto;
import com.storrity.storrity.security.entity.AppUser;
import com.storrity.storrity.security.entity.AppUserQueryParams;
import com.storrity.storrity.security.entity.UserProfile;
import com.storrity.storrity.security.entity.UserRole;
import com.storrity.storrity.security.event.PasswordChangedEvent;
import com.storrity.storrity.security.event.PasswordResetEvent;
import com.storrity.storrity.security.event.UserCreatedEvent;
import com.storrity.storrity.security.event.UserDeletedEvent;
import com.storrity.storrity.security.event.UserRoleChangedEvent;
import com.storrity.storrity.security.repository.UserProfileRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class AppUserServiceImpl implements AppUserService{
    
    private final UserRepository userRepo;
    private final UserRoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher publisher;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public AppUserServiceImpl(UserRepository userRepo, UserRoleRepository roleRepo
            , PasswordEncoder encoder, ApplicationEventPublisher publisher
            , UserProfileRepository userProfileRepository) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.publisher = publisher;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    @Transactional
    public UserDetailDto create(UserCreationDto dto) {
        AppUser user = AppUser.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .build();

//        UserRole role = roleRepo.findById(dto.getRole())
//                .orElseThrow(() -> new ResourceNotFoundAppException("Invalid role ID" + dto.getRole()));
//        user.setRole(role);

        Set<UserRole> requestedRoles = resolveRoles(dto.getRoles());
        protectSuperAdminAssignment(requestedRoles);
        user.setRoles(requestedRoles);

        AppUser savedUser = userRepo.save(user);
        
        UserProfile profile = UserProfile.builder()
                .username(dto.getUsername())
                .build();
        
        userProfileRepository.save(profile);

        UserDetailDto result = UserDetailDto.from(savedUser);
        publisher.publishEvent(new UserCreatedEvent(result));
        return result;
    }

    @Override
    @Transactional
    public UserDetailDto changePassword(PasswordChangeDto dto) {
        // Get the currently authenticated username from SecurityContext
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("Username: " + username);
        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        AppUser user = userRepo.findByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(encoder.encode(dto.getNewPassword()));
        AppUser savedUser = userRepo.save(user);

        UserDetailDto result = UserDetailDto.from(savedUser);
        publisher.publishEvent(new PasswordChangedEvent(result));
        return result;
    }

    @Override
    @Transactional
    public UserDetailDto resetPassword(PasswordRestDto dto) {
        AppUser user = userRepo.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundAppException("User not found with username: " + dto.getUsername()));

        user.setPassword(encoder.encode(dto.getNewPassword()));
        AppUser savedUser = userRepo.save(user);

        UserDetailDto result = UserDetailDto.from(savedUser);
        publisher.publishEvent(new PasswordResetEvent(result));
        return result;
    }

    @Override
    @Transactional
    public UserDetailDto changeRole(RoleChangeDto dto) {
        AppUser user = userRepo.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundAppException("User not found with username: " + dto.getUsername()));

        UserDetailDto previous = UserDetailDto.from(user);
        Set<UserRole> requestedRoles = resolveRoles(dto.getRoles());
        protectSuperAdminAssignment(requestedRoles);
        boolean currentlySuperAdmin = user.getRoles().stream().anyMatch(role -> "SUPER_ADMIN".equals(role.getId()));
        boolean remainsSuperAdmin = requestedRoles.stream().anyMatch(role -> "SUPER_ADMIN".equals(role.getId()));
        if (currentlySuperAdmin && !remainsSuperAdmin && userRepo.countByRoles_Id("SUPER_ADMIN") <= 1) {
            throw new IllegalArgumentException("Cannot remove the final SUPER_ADMIN assignment");
        }
        user.setRoles(requestedRoles);
        AppUser savedUser = userRepo.save(user);

        UserDetailDto updated = UserDetailDto.from(savedUser);
        publisher.publishEvent(new UserRoleChangedEvent(updated, previous));
        return updated;
    }
    
    public AppUser fetchRaw(String username) {
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundAppException("User not found with username: " + username));
        return user;
    }

    @Override
    public UserDetailDto fetch(String username) {
        AppUser user = fetchRaw(username);
        return UserDetailDto.from(user);
    }

    @Override
    public List<UserDetailDto> list(AppUserQueryParams params) {
        List<AppUser> users = userRepo.list(params);
        return users.stream().map(UserDetailDto::from).collect(Collectors.toList());
    }

    @Override
    public CountDto count(AppUserQueryParams params) {
        Long count = userRepo.countRecords(params);
        return new CountDto(count);
    }

    @Override
    public UserDetailDto delete(String role) {
        AppUser user = fetchRaw(role);
        userRepo.delete(user);
        UserDetailDto result = UserDetailDto.from(user);
        publisher.publishEvent(new UserDeletedEvent(result));
        return UserDetailDto.from(user);
    }
    
    private void protectSuperAdminAssignment(Set<UserRole> requestedRoles) {
        boolean assigningSuperAdmin = requestedRoles.stream().anyMatch(role -> "SUPER_ADMIN".equals(role.getId()));
        if (!assigningSuperAdmin || userRepo.count() == 0) return; // permit the one-time root-user bootstrap
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean allowed = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "USER_SUPER_ADMIN_ASSIGN".equals(authority.getAuthority()));
        if (!allowed) throw new IllegalArgumentException("Only an authorized Super Admin can assign the SUPER_ADMIN role");
    }

    private Set<UserRole> resolveRoles(Set<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) throw new IllegalArgumentException("At least one role must be assigned");
        List<UserRole> roles = roleRepo.findAllById(roleIds);
        Set<String> found = roles.stream().map(UserRole::getId).collect(Collectors.toSet());
        Set<String> missing = new java.util.LinkedHashSet<>(roleIds);
        missing.removeAll(found);
        if (!missing.isEmpty()) throw new ResourceNotFoundAppException("Invalid role IDs: " + missing);
        return new LinkedHashSet<>(roles);
    }
}
