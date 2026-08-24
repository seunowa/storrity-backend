/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.stockmovement.report.repository;

import com.storrity.storrity.stockmovement.report.dto.DailyStockMovementSummaryDto;
import com.storrity.storrity.stockmovement.report.dto.HourlyStockMovementSummaryDto;
import com.storrity.storrity.stockmovement.report.dto.MonthlyStockMovementSummaryDto;
import com.storrity.storrity.stockmovement.report.dto.QuarterlyStockMovementSummaryDto;
import com.storrity.storrity.stockmovement.report.dto.StockMovementReportQueryParams;
import com.storrity.storrity.stockmovement.report.dto.StockMovementsByBrandDto;
import com.storrity.storrity.stockmovement.report.dto.StockMovementsByCategoryDto;
import com.storrity.storrity.stockmovement.report.dto.StockMovementsByDirectionDto;
import com.storrity.storrity.stockmovement.report.dto.StockMovementsByMovementTypeDto;
import com.storrity.storrity.stockmovement.report.dto.StockMovementsByProductCodeDto;
import com.storrity.storrity.stockmovement.report.dto.StockMovementsByProductIdDto;
import com.storrity.storrity.stockmovement.report.dto.StockMovementsByStoreDto;
import com.storrity.storrity.stockmovement.report.dto.WeeklyStockMovementSummaryDto;
import com.storrity.storrity.stockmovement.report.dto.YearlyStockMovementSummaryDto;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface StockMovementReportRepository {

    List<HourlyStockMovementSummaryDto> hourlyStockMovementSummary(
            StockMovementReportQueryParams params);

    List<DailyStockMovementSummaryDto> dailyStockMovementSummary(
            StockMovementReportQueryParams params);

    List<WeeklyStockMovementSummaryDto> weeklyStockMovementSummary(
            StockMovementReportQueryParams params);

    List<MonthlyStockMovementSummaryDto> monthlyStockMovementSummary(
            StockMovementReportQueryParams params);

    List<QuarterlyStockMovementSummaryDto> quarterlyStockMovementSummary(
            StockMovementReportQueryParams params);

    List<YearlyStockMovementSummaryDto> yearlyStockMovementSummary(
            StockMovementReportQueryParams params);

    List<StockMovementsByStoreDto> stockMovementsByStore(
            StockMovementReportQueryParams params);

    List<StockMovementsByProductIdDto> stockMovementsByProductId(
            StockMovementReportQueryParams params);

    List<StockMovementsByProductCodeDto> stockMovementsByProductCode(
            StockMovementReportQueryParams params);

    List<StockMovementsByCategoryDto> stockMovementsByCategory(
            StockMovementReportQueryParams params);

    List<StockMovementsByBrandDto> stockMovementsByBrand(
            StockMovementReportQueryParams params);

    List<StockMovementsByMovementTypeDto> stockMovementsByMovementType(
            StockMovementReportQueryParams params);

    List<StockMovementsByDirectionDto> stockMovementsByDirection(
            StockMovementReportQueryParams params);
    
}
