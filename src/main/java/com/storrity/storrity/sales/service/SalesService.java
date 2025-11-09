/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.sales.service;

import com.storrity.storrity.sales.dto.SaleDto;
import com.storrity.storrity.sales.dto.SalesCreationDto;
import com.storrity.storrity.sales.dto.SalesCreationResponse;
import com.storrity.storrity.sales.entity.SaleQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface SalesService {    
//    public SaleDto create(SaleCreationDto dto);
    public SalesCreationResponse create(SalesCreationDto dto);
    public SaleDto fetch(UUID id);
    public List<SaleDto> list(SaleQueryParams params);
    public CountDto count(SaleQueryParams params);
}
