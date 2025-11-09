/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cashaccounts.repository;

import com.storrity.storrity.cashaccounts.entity.StatementItem;
import com.storrity.storrity.cashaccounts.entity.StatementItemQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface StatementItemRepositoryCustom {
    public List<StatementItem> list(StatementItemQueryParams params);
    public Long countRecords (StatementItemQueryParams params);
}
