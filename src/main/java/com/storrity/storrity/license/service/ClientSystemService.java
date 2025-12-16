/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.license.service;

import com.storrity.storrity.license.dto.ClientSystemCreationDto;
import com.storrity.storrity.license.dto.ClientSystemUpdateDto;
import com.storrity.storrity.license.entity.ClientSystem;
import com.storrity.storrity.license.repository.ClientSystemQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface ClientSystemService {
    public ClientSystem create(ClientSystemCreationDto dto);
    public ClientSystem fetch(UUID id);
    public ClientSystem fetchByClientId(String clientId);
    public List<ClientSystem> list(ClientSystemQueryParams params);
    public CountDto count(ClientSystemQueryParams params);
    public ClientSystem update(UUID id, ClientSystemUpdateDto dto);
    public ClientSystem delete(UUID id);
}
