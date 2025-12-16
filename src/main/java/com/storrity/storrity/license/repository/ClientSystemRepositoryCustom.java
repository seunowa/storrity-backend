/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.license.repository;

import com.storrity.storrity.license.entity.ClientSystem;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface ClientSystemRepositoryCustom {    
    public List<ClientSystem> list(ClientSystemQueryParams params);
    public Long countRecords (ClientSystemQueryParams params);
}
