/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.entity;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class SalesReturnQueryParams {
    
    private List<UUID> ids; 
    private List<String> transactionRefs;
    private List<UUID> saleIds;
    private List<String> performedBy;
    @Size(min = 2, max = 2, message = "createdAtRange must contain exactly two dates")
    private List<LocalDateTime> createdAtRange;
    @Size(min = 2, max = 2, message = "updatedAtRange must contain exactly two dates")
    private List<LocalDateTime> updatedAtRange;
    private Integer offset;
    private Integer limit;
    private String searchPhrase;
    private String sort;
}
