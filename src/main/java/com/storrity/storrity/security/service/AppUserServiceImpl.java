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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

        UserRole role = roleRepo.findById(dto.getRole())
                .orElseThrow(() -> new ResourceNotFoundAppException("Invalid role ID" + dto.getRole()));
        user.setRole(role);

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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        AppUser user = userRepo.findByUsername(username)
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

        UserRole role = roleRepo.findById(dto.getRole())
                .orElseThrow(() -> new ResourceNotFoundAppException("Invalid role ID" + dto.getRole()));

        user.setRole(role);
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
}
