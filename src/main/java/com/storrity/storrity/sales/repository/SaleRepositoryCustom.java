/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.sales.repository;

import com.storrity.storrity.sales.entity.Sale;
import com.storrity.storrity.sales.entity.SaleQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface SaleRepositoryCustom {   
    public List<Sale> list(SaleQueryParams params);
    public Long countRecords (SaleQueryParams params);
}
