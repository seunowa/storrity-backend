/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.pos.repository;

import com.storrity.storrity.pos.entity.Pos;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seun Owa
 */
public interface PosRepository extends JpaRepository<Pos, UUID>,  PosRepositoryCustom{
    
}
