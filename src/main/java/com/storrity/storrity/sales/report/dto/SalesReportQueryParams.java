/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SalesReportQueryParams {
    private List<UUID> storeIds;
    private List<UUID> productIds;
    private List<UUID> customerIds;
    private List<String> productCodes;
    private List<String> productCategories;
    private List<String> productSubCategories;
    private List<String> performedBy;
    private List<String> clientSystemIds;
    @Size(min = 2, max = 2, message = "createdAtRange must contain exactly two dates")
    private List<LocalDateTime> createdAtRange;
    private Integer offset;
    private Integer limit;
//    private String searchPhrase;
    private String sort;// raw sort string, e.g., "name:asc,createdAt:desc"
}
