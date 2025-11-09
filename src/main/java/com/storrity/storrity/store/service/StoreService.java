/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.store.service;

import com.storrity.storrity.store.dto.StoreCreationDto;
import com.storrity.storrity.store.dto.StoreUpdateDto;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.entity.StoreQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface StoreService {
    public Store create(StoreCreationDto dto);
    public Store fetch(UUID id);
    public List<Store> list(StoreQueryParams params);
    public CountDto count(StoreQueryParams params);
    public Store update(UUID id, StoreUpdateDto dto);
    public Store delete(UUID id);
}
