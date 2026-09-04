/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.service;

import com.storrity.storrity.sales.report.dto.*;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
@Deprecated
//@Component
public class SalesReportToolsGrouped {
    
    public enum SalesSummaryPeriod {

        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY

    }
    
    public enum SalesBreakdownType {

        STORE,
        PRODUCT,
        CATEGORY,
        BRAND,
        CASHIER,
        CUSTOMER,
        HOUR,
        WEEKDAY

    }

    private final SalesReportService salesReportService;

    public SalesReportToolsGrouped(SalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }

    // =====================================================
    // Helper – builds the query params object
    // =====================================================
    private SalesReportQueryParams buildParams(
            LocalDate from,
            LocalDate to,
            List<UUID> storeIds,
            List<UUID> productIds,
            List<String> productCategories,
            List<String> performedBy,
            Integer limit) {

        SalesReportQueryParams params = new SalesReportQueryParams();

        if (from != null && to != null) {
            params.setCreatedAtRange(List.of(
                    from.atStartOfDay(),
                    to.atTime(LocalTime.MAX)
            ));
        }

        if (storeIds != null && !storeIds.isEmpty()) {
            params.setStoreIds(storeIds);
        }
        if (productIds != null && !productIds.isEmpty()) {
            params.setProductIds(productIds);
        }
        if (productCategories != null && !productCategories.isEmpty()) {
            params.setProductCategories(productCategories);
        }
        if (performedBy != null && !performedBy.isEmpty()) {
            params.setPerformedBy(performedBy);
        }
        if (limit != null) {
            params.setLimit(limit);
        }

        return params;
    }
    
    
    // =====================================================
    // SALES SUMMARY
    // =====================================================

    @Tool(description = """
            Returns a sales summary aggregated over a time period.

            Choose this tool whenever the user asks about:

            • sales today
            • sales yesterday
            • daily sales
            • weekly sales
            • monthly sales
            • quarterly sales
            • yearly sales
            • revenue trend
            • sales trend
            • historical sales performance

            The summaryPeriod determines the level of aggregation.

            DAILY
            WEEKLY
            MONTHLY
            QUARTERLY
            YEARLY

            The returned object depends on the selected period.

            DAILY -> List<DailySalesSummaryDto>

            WEEKLY -> List<WeeklySalesSummaryDto>

            MONTHLY -> List<MonthlySalesSummaryDto>

            QUARTERLY -> List<QuarterlySalesSummaryDto>

            YEARLY -> List<YearlySalesSummaryDto>
            """)
    public Object getSalesSummary(

            @ToolParam(description = "Time aggregation level: DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY") SalesSummaryPeriod summaryPeriod,
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive.",required = false) LocalDate from,
            @ToolParam( description = "End date (yyyy-MM-dd), inclusive.", required = false) LocalDate to,
            @ToolParam( description = "Optional store IDs.", required = false) List<UUID> storeIds,
            @ToolParam( description = "Optional product IDs.", required = false) List<UUID> productIds,
            @ToolParam( description = "Optional product categories.", required = false) List<String> productCategories,
            @ToolParam( description = "Optional cashier/staff names.", required = false) List<String> performedBy,
            @ToolParam( description = "Maximum rows to return.", required = false) Integer limit) {
        
        SalesReportQueryParams params = buildParams( from, to, storeIds, productIds, productCategories, performedBy, limit);

        return switch (summaryPeriod) {
            case DAILY ->
                    salesReportService.dailySalesSummary(params);
            case WEEKLY ->
                    salesReportService.weeklySalesSummary(params);
            case MONTHLY ->
                    salesReportService.monthlySalesSummary(params);
            case QUARTERLY ->
                    salesReportService.quarterlySalesSummary(params);
            case YEARLY ->
                    salesReportService.yearlySalesSummary(params);
        };
    }
    
    // =====================================================
    // SALES BREAKDOWN
    // =====================================================

    @Tool(description = """
            Returns sales grouped by a business dimension.

            Choose this tool whenever the user asks questions like:

            • sales by store
            • top stores
            • best performing stores

            • sales by product
            • top products

            • sales by category

            • sales by brand

            • sales by cashier
            • best cashier

            • sales by customer
            • top customers

            • busiest hours
            • peak sales hours

            • busiest weekdays

            The breakdownType determines how the sales are grouped.

            STORE

            PRODUCT

            CATEGORY

            BRAND

            CASHIER

            CUSTOMER

            HOUR

            WEEKDAY

            The returned object depends on the selected breakdown.

            STORE -> List<SalesByStoreDto>

            PRODUCT -> List<SalesByProductDto>

            CATEGORY -> List<SalesByCategoryDto>

            BRAND -> List<SalesByBrandDto>

            CASHIER -> List<SalesByCashierDto>

            CUSTOMER -> List<SalesByCustomerDto>

            HOUR -> List<SalesByHourDto>

            WEEKDAY -> List<SalesByWeekdayDto>
            """)
    public Object getSalesBreakdown(
            @ToolParam(description = " Breakdown dimension: STORE, PRODUCT, CATEGORY, BRAND, CASHIER, CUSTOMER, HOUR, WEEKDAY") SalesBreakdownType breakdownType,
            @ToolParam( description = "Start date (yyyy-MM-dd), inclusive.", required = false) LocalDate from,
            @ToolParam( description = "End date (yyyy-MM-dd), inclusive.", required = false) LocalDate to,
            @ToolParam( description = "Optional store IDs.", required = false) List<UUID> storeIds,
            @ToolParam( description = "Optional product IDs.", required = false) List<UUID> productIds,
            @ToolParam( description = "Optional product categories.", required = false) List<String> productCategories,
            @ToolParam( description = "Optional cashier/staff names.", required = false) List<String> performedBy,
            @ToolParam( description = "Maximum rows to return.", required = false) Integer limit) {
        
        SalesReportQueryParams params = buildParams( from, to, storeIds, productIds, productCategories, performedBy, limit);

        return switch (breakdownType) {

            case STORE ->
                    salesReportService.salesByStore(params);

            case PRODUCT ->
                    salesReportService.salesByProduct(params);

            case CATEGORY ->
                    salesReportService.salesByCategory(params);

            case BRAND ->
                    salesReportService.salesByBrand(params);

            case CASHIER ->
                    salesReportService.salesByCashier(params);

            case CUSTOMER ->
                    salesReportService.salesByCustomer(params);

            case HOUR ->
                    salesReportService.salesByHour(params);

            case WEEKDAY ->
                    salesReportService.salesByWeekday(params);
        };
    }

    // =====================================================
    // BASKET ANALYSIS
    // =====================================================

    @Tool(description = """
            Returns overall basket analysis metrics.

            Choose this tool whenever the user asks:

            • average basket

            • average basket value

            • average order value

            • average transaction value

            • basket statistics

            • basket analysis

            • customer buying behaviour

            • purchase behaviour

            • average items purchased

            • discount penetration

            Returns a single AverageBasketDto containing:

            • basket counts

            • revenue totals

            • average basket value

            • median basket value

            • basket composition

            • average discount

            • average tax

            • average unique products

            • discount penetration metrics
            """)
    public AverageBasketDto getAverageBasket(
            @ToolParam( description = "Start date (yyyy-MM-dd), inclusive.", required = false) LocalDate from,
            @ToolParam( description = "End date (yyyy-MM-dd), inclusive.", required = false) LocalDate to,
            @ToolParam( description = "Optional store IDs.", required = false) List<UUID> storeIds,
            @ToolParam( description = "Optional product IDs.", required = false) List<UUID> productIds,
            @ToolParam( description = "Optional product categories.", required = false) List<String> productCategories,
            @ToolParam( description = "Optional cashier/staff names.", required = false) List<String> performedBy,
            @ToolParam( description = "Maximum rows to consider.", required = false) Integer limit) {
        
        SalesReportQueryParams params = buildParams( from, to, storeIds, productIds, productCategories, performedBy, limit);
        return salesReportService.averageBasket(params);
    }

}
