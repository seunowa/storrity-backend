/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.sales.repository;
import com.storrity.storrity.sales.entity.SalesReturn;
import com.storrity.storrity.sales.entity.SalesReturnQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface SalesReturnRepositoryCustom {
       
    public List<SalesReturn> list(SalesReturnQueryParams params);
    public Long countRecords (SalesReturnQueryParams params);
}
