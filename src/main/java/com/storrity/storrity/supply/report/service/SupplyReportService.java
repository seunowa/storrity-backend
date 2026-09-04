package com.storrity.storrity.supply.report.service;

import com.storrity.storrity.supply.report.dto.DeliveryVarianceByProductDto;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceBySupplierDto;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceDto;
import com.storrity.storrity.supply.report.dto.ProductProcurementSummaryDto;
import com.storrity.storrity.supply.report.dto.SupplierLeadTimeDto;
import com.storrity.storrity.supply.report.dto.SupplierPerformanceDto;
import com.storrity.storrity.supply.report.dto.SupplyReportQueryParams;
import com.storrity.storrity.supply.report.dto.SupplyStatusSummaryDto;
import java.util.List;

/**
 * Service for supply/procurement reporting.
 *
 * @author Seun Owa
 */
public interface SupplyReportService {

    List<SupplyStatusSummaryDto> supplyStatusSummary(SupplyReportQueryParams params);

    List<DeliveryVarianceDto> deliveryVariance(SupplyReportQueryParams params);

    List<SupplierPerformanceDto> supplierPerformance(SupplyReportQueryParams params);

    List<DeliveryVarianceByProductDto> deliveryVarianceByProduct(SupplyReportQueryParams params);

    List<DeliveryVarianceBySupplierDto> deliveryVarianceBySupplier(SupplyReportQueryParams params);

    List<SupplierLeadTimeDto> supplierLeadTime(SupplyReportQueryParams params);

    List<ProductProcurementSummaryDto> productProcurementSummary(SupplyReportQueryParams params);
}
