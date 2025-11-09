/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.salesattendant.repository;

import com.storrity.storrity.salesattendant.entity.SalesAttendant;
import com.storrity.storrity.salesattendant.entity.SalesAttendantQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface SalesAttendantRepositoryCustom {  
    public List<SalesAttendant> list(SalesAttendantQueryParams params);
    public Long countRecords (SalesAttendantQueryParams params);
}
