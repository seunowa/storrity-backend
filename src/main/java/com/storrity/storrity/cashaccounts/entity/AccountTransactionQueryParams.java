/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.entity;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;

/**
 *
 * @author Seun Owa
 */
@Data
public class AccountTransactionQueryParams {        
    private List<UUID> ids;
    private List<String> transactionRefs;
    private List<String> performedBy;
    private List<String> tags;
    @Size(min = 2, max = 2, message = "createdAtRange must contain exactly two dates")
    private List<LocalDateTime> createdAtRange;
    @Size(min = 2, max = 2, message = "updatedAtRange must contain exactly two dates")
    private List<LocalDateTime> updatedAtRange;
    private Integer offset;
    private Integer limit;
    private String searchPhrase;
    private String sort;
}
