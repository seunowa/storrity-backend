/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cashaccounts.service;

import com.storrity.storrity.cashaccounts.entity.StatementItem;
import com.storrity.storrity.cashaccounts.entity.StatementItemQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface StatementItemService {
    public StatementItem fetch(UUID id);
    public List<StatementItem> list(StatementItemQueryParams params);
    public CountDto count(StatementItemQueryParams params);
}
