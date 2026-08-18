/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.dto;

import com.storrity.storrity.supply.entity.SupplyStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Data
@SuperBuilder
public class SupplyStatusUpdateDtoStale{
    private SupplyStatus supplyStatus;
}
