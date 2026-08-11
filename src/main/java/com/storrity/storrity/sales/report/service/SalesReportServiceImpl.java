/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.service;

import com.storrity.storrity.sales.report.dto.AverageBasketDto;
import com.storrity.storrity.sales.report.dto.DailySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.HourlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.MonthlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.QuarterlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.SalesByBrandDto;
import com.storrity.storrity.sales.report.dto.SalesByCashierDto;
import com.storrity.storrity.sales.report.dto.SalesByCategoryDto;
import com.storrity.storrity.sales.report.dto.SalesByClientSystemDto;
import com.storrity.storrity.sales.report.dto.SalesByCustomerDto;
import com.storrity.storrity.sales.report.dto.SalesByHourDto;
import com.storrity.storrity.sales.report.dto.SalesByMonthDto;
import com.storrity.storrity.sales.report.dto.SalesByProductDto;
import com.storrity.storrity.sales.report.dto.SalesByStoreDto;
import com.storrity.storrity.sales.report.dto.SalesByWeekdayDto;
import com.storrity.storrity.sales.report.dto.SalesReportQueryParams;
import com.storrity.storrity.sales.report.dto.WeeklySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.YearlySalesSummaryDto;
import com.storrity.storrity.sales.report.repository.SalesReportRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class SalesReportServiceImpl implements SalesReportService{
    
    private final SalesReportRepository salesReportRepository;

    @Override
    public List<HourlySalesSummaryDto> hourlySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.hourlySalesSummary(params);
    }   

    @Autowired
    public SalesReportServiceImpl(SalesReportRepository salesReportRepository) {
        this.salesReportRepository = salesReportRepository;
    }

    @Override
    public List<DailySalesSummaryDto> dailySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.dailySalesSummary(params);
    }

    @Override
    public List<WeeklySalesSummaryDto> weeklySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.weeklySalesSummary(params);
    }

    @Override
    public List<MonthlySalesSummaryDto> monthlySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.monthlySalesSummary(params);
    }

    @Override
    public List<QuarterlySalesSummaryDto> quarterlySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.quarterlySalesSummary(params);
    }

    @Override
    public List<YearlySalesSummaryDto> yearlySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.yearlySalesSummary(params);
    }

    @Override
    public List<SalesByStoreDto> salesByStore(SalesReportQueryParams params) {
        return salesReportRepository.salesByStore(params);
    }

    @Override
    public List<SalesByProductDto> salesByProduct(SalesReportQueryParams params) {
        return salesReportRepository.salesByProduct(params);
    }

    @Override
    public List<SalesByCategoryDto> salesByCategory(SalesReportQueryParams params) {
        return salesReportRepository.salesByCategory(params);
    }

    @Override
    public List<SalesByBrandDto> salesByBrand(SalesReportQueryParams params) {
        return salesReportRepository.salesByBrand(params);
    }

    @Override
    public List<SalesByCashierDto> salesByCashier(SalesReportQueryParams params) {
        return salesReportRepository.salesByCashier(params);
    }

    @Override
    public List<SalesByClientSystemDto> salesByClientSystem(SalesReportQueryParams params) {
        return salesReportRepository.salesByClientSystem(params);
    }

    @Override
    public List<SalesByCustomerDto> salesByCustomer(SalesReportQueryParams params) {
        return salesReportRepository.salesByCustomer(params);
    }

    @Override
    public List<SalesByHourDto> salesByHour(SalesReportQueryParams params) {
        return salesReportRepository.salesByHour(params);
    }

    @Override
    public List<SalesByMonthDto> salesByMonth(SalesReportQueryParams params) {
        return salesReportRepository.salesByMonth(params);
    }

    @Override
    public List<SalesByWeekdayDto> salesByWeekday(SalesReportQueryParams params) {
        return salesReportRepository.salesByWeekday(params);
    }

    @Override
    public AverageBasketDto averageBasket(SalesReportQueryParams params) {
        return salesReportRepository.averageBasket(params);
    }

}
