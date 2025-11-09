/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.cashaccounts.repository;

import com.storrity.storrity.cashaccounts.entity.CashAccount;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Seun Owa
 */
public interface CashAccountRepository extends JpaRepository<CashAccount, String>, CashAccountRepositoryCustom{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CashAccount c WHERE c.id = :id")
    Optional<CashAccount> findByIdForUpdate(@Param("id") String id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CashAccount c WHERE c.id IN :ids")
        List<CashAccount> findAllByIdForUpdate(@Param("ids") Collection<String> ids);
    }
