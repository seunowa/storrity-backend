/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.store.repository;

import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.entity.StoreQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface StoreRepositoryCustom {
    public List<Store> list(StoreQueryParams params);
    public Long countRecords (StoreQueryParams params);
}
