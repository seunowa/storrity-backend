/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.supply.report.repository;

import com.storrity.storrity.supply.report.dto.DeliveryVarianceByProductDto;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceDto;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceBySupplierDto;
import com.storrity.storrity.supply.report.dto.ProductProcurementSummaryDto;
import com.storrity.storrity.supply.report.dto.SupplierLeadTimeDto;
import com.storrity.storrity.supply.report.dto.SupplierPerformanceDto;
import com.storrity.storrity.supply.report.dto.SupplyReportQueryParams;
import com.storrity.storrity.supply.report.dto.SupplyStatusSummaryDto;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface SupplyReportRepository {

    /**
     * Purchase orders grouped by supplyStatus.
     */
    List<SupplyStatusSummaryDto> supplyStatusSummary(SupplyReportQueryParams params);

    /**
     * Per supply-item variance between quantity ordered and received.
     */
    List<DeliveryVarianceDto> deliveryVariance(SupplyReportQueryParams params);

    /**
     * On-time delivery rate and average delay per supplier.
     */
    List<SupplierPerformanceDto> supplierPerformance(SupplyReportQueryParams params);

    /**
     * Per supply-item variance between quantity ordered and received grouped by product.
     */
    List<DeliveryVarianceByProductDto> deliveryVarianceByProduct(SupplyReportQueryParams params);

    /**
     * Per supply-item variance between quantity ordered and received grouped by supplier.
     */
    List<DeliveryVarianceBySupplierDto> deliveryVarianceBySupplier(SupplyReportQueryParams params);

    /**
     * Order-to-receipt lead time distribution per supplier.
     */
    List<SupplierLeadTimeDto> supplierLeadTime(SupplyReportQueryParams params);

    /**
     * What products are we buying, how much are we buying, and from whom.
     */
    List<ProductProcurementSummaryDto> productProcurementSummary(SupplyReportQueryParams params);
    
}
