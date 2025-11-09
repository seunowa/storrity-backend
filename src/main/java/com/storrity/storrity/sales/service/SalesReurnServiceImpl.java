/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.service;

import com.storrity.storrity.sales.dto.SalesReturnCreationDto;
import com.storrity.storrity.sales.dto.SalesReturnDto;
import com.storrity.storrity.sales.entity.SalesReturnQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class SalesReurnServiceImpl implements SalesReturnService{

    @Override
    public SalesReturnDto create(SalesReturnCreationDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public SalesReturnDto fetch(UUID id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SalesReturnDto> list(SalesReturnQueryParams params) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CountDto count(SalesReturnQueryParams params) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
