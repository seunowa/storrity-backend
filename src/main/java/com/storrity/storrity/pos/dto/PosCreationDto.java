/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.pos.dto;

import com.storrity.storrity.pos.entity.PosStatus;
import java.util.UUID;
import lombok.Data;


/**
 *
 * @author Seun Owa
 */
@Data
public class PosCreationDto {
    private String name;
    private String identifier;
    private PosStatus status;
    private UUID storeId;
}
