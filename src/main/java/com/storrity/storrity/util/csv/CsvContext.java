/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.util.csv;

import java.util.UUID;
import lombok.Builder;

/**
 *
 * @author Seun Owa
 */
@Builder
public class CsvContext {
    private UUID storeId;
//    private UUID tenantId;
//    private UUID companyId;

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
    
    
}
