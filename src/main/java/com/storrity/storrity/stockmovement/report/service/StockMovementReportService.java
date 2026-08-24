/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stockmovement.report.service;

import com.storrity.storrity.stockmovement.report.dto.*;
import java.util.List;

/**
 *
 * @author Seun Owa
 */


public interface StockMovementReportService {
    public List<HourlyStockMovementSummaryDto> hourlyStockMovementSummary(StockMovementReportQueryParams params);

    public List<DailyStockMovementSummaryDto> dailyStockMovementSummary(StockMovementReportQueryParams params);

    public List<WeeklyStockMovementSummaryDto> weeklyStockMovementSummary(StockMovementReportQueryParams params);

    public List<MonthlyStockMovementSummaryDto> monthlyStockMovementSummary(StockMovementReportQueryParams params);

    public List<QuarterlyStockMovementSummaryDto> quarterlyStockMovementSummary(StockMovementReportQueryParams params);

    public List<YearlyStockMovementSummaryDto> yearlyStockMovementSummary(StockMovementReportQueryParams params);

    public List<StockMovementsByStoreDto> stockMovementsByStore(StockMovementReportQueryParams params);

    public List<StockMovementsByProductIdDto> stockMovementsByProductId(StockMovementReportQueryParams params);

    public List<StockMovementsByProductIdDto> stockMovementsByProductCode(StockMovementReportQueryParams params);

    public List<StockMovementsByCategoryDto> stockMovementsByCategory(StockMovementReportQueryParams params);

    public List<StockMovementsByBrandDto> stockMovementsByBrand(StockMovementReportQueryParams params);

    public List<StockMovementsByMovementTypeDto> stockMovementsByMovementType(StockMovementReportQueryParams params);

    public List<StockMovementsByDirectionDto> stockMovementsByDirection(StockMovementReportQueryParams params);

}
