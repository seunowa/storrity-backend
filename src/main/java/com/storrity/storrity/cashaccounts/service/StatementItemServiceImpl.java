/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.service;

import com.storrity.storrity.cashaccounts.entity.StatementItem;
import com.storrity.storrity.cashaccounts.entity.StatementItemQueryParams;
import com.storrity.storrity.cashaccounts.repository.StatementItemRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class StatementItemServiceImpl implements StatementItemService{

    private final StatementItemRepository statementItemRepository;
    
    @Autowired
    public StatementItemServiceImpl(StatementItemRepository statementItemRepository) {
        this.statementItemRepository = statementItemRepository;
    }

    @Override
    public StatementItem fetch(UUID id) {
        return statementItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundAppException("Statement item not found with id: " + id));
    }

    @Override
    public List<StatementItem> list(StatementItemQueryParams params) {
        return statementItemRepository.list(params);
    }

    @Override
    public CountDto count(StatementItemQueryParams params) {
        return CountDto
                .builder()
                .count(statementItemRepository.countRecords(params))
                .build();
    }
    
}
