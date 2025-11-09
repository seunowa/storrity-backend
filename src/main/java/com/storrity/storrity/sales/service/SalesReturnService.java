/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.sales.service;

import com.storrity.storrity.sales.dto.SalesReturnCreationDto;
import com.storrity.storrity.sales.dto.SalesReturnDto;
import com.storrity.storrity.sales.entity.SalesReturnQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface SalesReturnService {    
    public SalesReturnDto create(SalesReturnCreationDto dto);
    public SalesReturnDto fetch(UUID id);
    public List<SalesReturnDto> list(SalesReturnQueryParams params);
    public CountDto count(SalesReturnQueryParams params);
}
