/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.repository;

import com.storrity.storrity.security.entity.UserProfile;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Seun Owa
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID>, UserProfileRepositoryCustom{
    Optional<UserProfile> findByUsername(String username);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM UserProfile p WHERE p.username = :username")
    Optional<UserProfile> findByUsernameForUpdate(@Param("username") String username);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM UserProfile p WHERE p.username IN :usernames")
        List<UserProfile> findAllByUsernameForUpdate(@Param("username") Collection<String> usernames);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM UserProfile p WHERE p.id = :id")
    Optional<UserProfile> findByIdForUpdate(@Param("id") UUID id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM UserProfile p WHERE p.id IN :ids")
        List<UserProfile> findAllByIdForUpdate(@Param("ids") Collection<UUID> ids);
}
