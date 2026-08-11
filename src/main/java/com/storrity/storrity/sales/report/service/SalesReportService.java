/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
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
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface SalesReportService {
    List<HourlySalesSummaryDto> hourlySalesSummary(SalesReportQueryParams params);
    List<DailySalesSummaryDto> dailySalesSummary(SalesReportQueryParams params);    
    List<WeeklySalesSummaryDto> weeklySalesSummary(SalesReportQueryParams params);
    List<MonthlySalesSummaryDto> monthlySalesSummary(SalesReportQueryParams params);
    List<QuarterlySalesSummaryDto> quarterlySalesSummary(SalesReportQueryParams params);
    List<YearlySalesSummaryDto> yearlySalesSummary(SalesReportQueryParams params);
    
    List<SalesByStoreDto> salesByStore(SalesReportQueryParams params);
    List<SalesByProductDto> salesByProduct(SalesReportQueryParams params);
    List<SalesByCategoryDto> salesByCategory(SalesReportQueryParams params);
    List<SalesByBrandDto> salesByBrand(SalesReportQueryParams params);
    List<SalesByCashierDto> salesByCashier(SalesReportQueryParams params);
    List<SalesByClientSystemDto> salesByClientSystem(SalesReportQueryParams params);
    List<SalesByCustomerDto> salesByCustomer(SalesReportQueryParams params);
    List<SalesByHourDto> salesByHour(SalesReportQueryParams params);
    List<SalesByMonthDto> salesByMonth(SalesReportQueryParams params);
    List<SalesByWeekdayDto> salesByWeekday(SalesReportQueryParams params);
//
//    DiscountReportDto discountReport(SalesReportQueryParams params);
    AverageBasketDto averageBasket(SalesReportQueryParams params);
//
//    AverageTransactionValueDto averageTransactionValue(SalesReportQueryParams params);
//
//    GrossVsNetSalesDto grossVsNetSales(SalesReportQueryParams params);
}
