/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stockmovement.report.service;

import com.storrity.storrity.stockmovement.report.dto.*;
import com.storrity.storrity.stockmovement.report.repository.StockMovementReportRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockMovementReportServiceImpl implements StockMovementReportService{

    private final StockMovementReportRepository repository;

    @Override
    public List<HourlyStockMovementSummaryDto> hourlyStockMovementSummary(StockMovementReportQueryParams params) {

        return repository.hourlyStockMovementSummary(params);
    }

    @Override
    public List<DailyStockMovementSummaryDto> dailyStockMovementSummary(StockMovementReportQueryParams params) {

        return repository.dailyStockMovementSummary(params);
    }

    @Override
    public List<WeeklyStockMovementSummaryDto> weeklyStockMovementSummary(StockMovementReportQueryParams params) {

        return repository.weeklyStockMovementSummary(params);
    }

    @Override
    public List<MonthlyStockMovementSummaryDto> monthlyStockMovementSummary(StockMovementReportQueryParams params) {

        return repository.monthlyStockMovementSummary(params);
    }

    @Override
    public List<QuarterlyStockMovementSummaryDto> quarterlyStockMovementSummary(StockMovementReportQueryParams params) {

        return repository.quarterlyStockMovementSummary(params);
    }

    @Override
    public List<YearlyStockMovementSummaryDto> yearlyStockMovementSummary(StockMovementReportQueryParams params) {

        return repository.yearlyStockMovementSummary(params);
    }

    @Override
    public List<StockMovementsByStoreDto> stockMovementsByStore(StockMovementReportQueryParams params) {

        return repository.stockMovementsByStore(params);
    }

    @Override
    public List<StockMovementsByProductIdDto> stockMovementsByProductId(StockMovementReportQueryParams params) {

        return repository.stockMovementsByProductId(params);
    }

    @Override
    public List<StockMovementsByProductIdDto> stockMovementsByProductCode(StockMovementReportQueryParams params) {

        return repository.stockMovementsByProductId(params);
    }

    @Override
    public List<StockMovementsByCategoryDto> stockMovementsByCategory(StockMovementReportQueryParams params) {

        return repository.stockMovementsByCategory(params);
    }

    @Override
    public List<StockMovementsByBrandDto> stockMovementsByBrand(StockMovementReportQueryParams params) {

        return repository.stockMovementsByBrand(params);
    }

    @Override
    public List<StockMovementsByMovementTypeDto> stockMovementsByMovementType(StockMovementReportQueryParams params) {

        return repository.stockMovementsByMovementType(params);
    }

    @Override
    public List<StockMovementsByDirectionDto> stockMovementsByDirection(StockMovementReportQueryParams params) {

        return repository.stockMovementsByDirection(params);
    }

}
