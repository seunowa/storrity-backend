/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.entity;

/**
 *
 * @author Seun Owa
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Registered application permission. The id is a stable authority key. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_permission")
public class UserPermission {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 100)
    private String id;

    @Column(name = "description", nullable = false, length = 255)
    private String description;
}
