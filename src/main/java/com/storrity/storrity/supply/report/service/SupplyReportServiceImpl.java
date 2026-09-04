package com.storrity.storrity.supply.report.service;

import com.storrity.storrity.supply.report.dto.DeliveryVarianceByProductDto;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceBySupplierDto;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceDto;
import com.storrity.storrity.supply.report.dto.ProductProcurementSummaryDto;
import com.storrity.storrity.supply.report.dto.SupplierLeadTimeDto;
import com.storrity.storrity.supply.report.dto.SupplierPerformanceDto;
import com.storrity.storrity.supply.report.dto.SupplyReportQueryParams;
import com.storrity.storrity.supply.report.dto.SupplyStatusSummaryDto;
import com.storrity.storrity.supply.report.repository.SupplyReportRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link SupplyReportService}.
 *
 * The service deliberately contains no report calculation logic. Report
 * queries and result mapping remain in {@link SupplyReportRepository}.
 *
 * @author Seun Owa
 */
@Service
public class SupplyReportServiceImpl implements SupplyReportService {

    private final SupplyReportRepository supplyReportRepository;

    @Autowired
    public SupplyReportServiceImpl(SupplyReportRepository supplyReportRepository) {
        this.supplyReportRepository = supplyReportRepository;
    }

    @Override
    public List<SupplyStatusSummaryDto> supplyStatusSummary(SupplyReportQueryParams params) {
        return supplyReportRepository.supplyStatusSummary(params);
    }

    @Override
    public List<DeliveryVarianceDto> deliveryVariance(SupplyReportQueryParams params) {
        return supplyReportRepository.deliveryVariance(params);
    }

    @Override
    public List<SupplierPerformanceDto> supplierPerformance(SupplyReportQueryParams params) {
        return supplyReportRepository.supplierPerformance(params);
    }

    @Override
    public List<DeliveryVarianceByProductDto> deliveryVarianceByProduct(SupplyReportQueryParams params) {
        return supplyReportRepository.deliveryVarianceByProduct(params);
    }

    @Override
    public List<DeliveryVarianceBySupplierDto> deliveryVarianceBySupplier(SupplyReportQueryParams params) {
        return supplyReportRepository.deliveryVarianceBySupplier(params);
    }

    @Override
    public List<SupplierLeadTimeDto> supplierLeadTime(SupplyReportQueryParams params) {
        return supplyReportRepository.supplierLeadTime(params);
    }

    @Override
    public List<ProductProcurementSummaryDto> productProcurementSummary(SupplyReportQueryParams params) {
        return supplyReportRepository.productProcurementSummary(params);
    }
}
