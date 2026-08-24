/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stockmovement.report.dto;

import com.storrity.storrity.stockmovement.entity.StockMovementType;
import com.storrity.storrity.stockmovement.entity.StockMoevmentDirection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Seun Owa
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementReportQueryParams {

    private List<UUID> storeIds;

    private List<UUID> productIds;

    /**
     * Product business identifier.
     *
     * Unlike productId, productCode identifies the same
     * product across different stores.
     */
    private List<String> productCodes;

    private List<String> productCategories;

    private List<String> productSubCategories;

    private List<String> productBrands;

    private List<StockMovementType> movementTypes;

    private List<StockMoevmentDirection> directions;

    private List<String> performedBy;

    private List<String> transactionRefs;

    private List<LocalDateTime> createdAtRange;

    private Integer offset;

    private Integer limit;
}