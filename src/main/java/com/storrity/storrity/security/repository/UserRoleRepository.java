/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.repository;

import com.storrity.storrity.security.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seun Owa
 */
public interface UserRoleRepository extends JpaRepository<UserRole, String>, UserRoleRepositoryCustom {
}
