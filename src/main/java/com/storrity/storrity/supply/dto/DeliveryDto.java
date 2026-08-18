/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Seun Owa
 */
@Data
public class DeliveryDto {
    private String deliveryNoteNumber;
    private String invoiceNumber;

    private List<DeliveryItemDto> items;
}
