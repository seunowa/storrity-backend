/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.service;

import com.storrity.storrity.sales.report.dto.*;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
@Component
public class SalesReportTools {

    private final SalesReportService salesReportService;

    public SalesReportTools(SalesReportService salesReportService) {
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
    // Time-based summaries
    // =====================================================

    @Tool(description = """
            Get daily sales summary.
            Returns sales aggregated by day (transactions, quantity, gross, discount, tax, net).
            Best for short periods or detailed daily performance.
            """)
    public List<DailySalesSummaryDto> getDailySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds,
            @ToolParam(description = "Maximum number of rows to return", required = false) Integer limit) {

        return salesReportService.dailySalesSummary(
                buildParams(from, to, storeIds, null, null, null, limit));
    }

    @Tool(description = """
            Get weekly sales summary.
            Returns sales aggregated by week.
            """)
    public List<WeeklySalesSummaryDto> getWeeklySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds,
            @ToolParam(description = "Maximum number of rows", required = false) Integer limit) {

        return salesReportService.weeklySalesSummary(
                buildParams(from, to, storeIds, null, null, null, limit));
    }

    @Tool(description = """
            Get monthly sales summary.
            Best tool for monthly trend charts and period comparisons.
            """)
    public List<MonthlySalesSummaryDto> getMonthlySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds,
            @ToolParam(description = "Maximum number of rows", required = false) Integer limit) {

        return salesReportService.monthlySalesSummary(
                buildParams(from, to, storeIds, null, null, null, limit));
    }

    @Tool(description = """
            Get quarterly sales summary.
            """)
    public List<QuarterlySalesSummaryDto> getQuarterlySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds) {

        return salesReportService.quarterlySalesSummary(
                buildParams(from, to, storeIds, null, null, null, null));
    }

    @Tool(description = """
            Get yearly sales summary.
            Useful for high-level yearly trends.
            """)
    public List<YearlySalesSummaryDto> getYearlySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds) {

        return salesReportService.yearlySalesSummary(
                buildParams(from, to, storeIds, null, null, null, null));
    }

    // =====================================================
    // Breakdowns
    // =====================================================

    @Tool(description = """
            Get sales broken down by store.
            Excellent for comparing store performance.
            """)
    public List<SalesByStoreDto> getSalesByStore(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds,
            @ToolParam(description = "Maximum number of stores to return", required = false) Integer limit) {

        return salesReportService.salesByStore(
                buildParams(from, to, storeIds, null, null, null, limit));
    }

    @Tool(description = """
            Get sales broken down by product.
            Use for top-selling products charts.
            """)
    public List<SalesByProductDto> getSalesByProduct(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds,
            @ToolParam(description = "Maximum number of products to return (recommended 10-15)", required = false) Integer limit) {

        return salesReportService.salesByProduct(
                buildParams(from, to, storeIds, null, null, null, limit));
    }

    @Tool(description = """
            Get sales broken down by product category.
            """)
    public List<SalesByCategoryDto> getSalesByCategory(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds,
            @ToolParam(description = "Maximum number of categories", required = false) Integer limit) {

        return salesReportService.salesByCategory(
                buildParams(from, to, storeIds, null, null, null, limit));
    }

    @Tool(description = """
            Get sales broken down by brand.
            """)
    public List<SalesByBrandDto> getSalesByBrand(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds,
            @ToolParam(description = "Maximum number of brands", required = false) Integer limit) {

        return salesReportService.salesByBrand(
                buildParams(from, to, storeIds, null, null, null, limit));
    }

    @Tool(description = """
            Get sales broken down by cashier / sales attendant.
            """)
    public List<SalesByCashierDto> getSalesByCashier(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds,
            @ToolParam(description = "Maximum number of cashiers", required = false) Integer limit) {

        return salesReportService.salesByCashier(
                buildParams(from, to, storeIds, null, null, null, limit));
    }

    @Tool(description = """
            Get sales broken down by customer.
            Useful for identifying top customers.
            """)
    public List<SalesByCustomerDto> getSalesByCustomer(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds,
            @ToolParam(description = "Maximum number of customers (recommended 10)", required = false) Integer limit) {

        return salesReportService.salesByCustomer(
                buildParams(from, to, storeIds, null, null, null, limit));
    }

    @Tool(description = """
            Get sales broken down by hour of day (0-23).
            Useful for identifying peak hours.
            """)
    public List<SalesByHourDto> getSalesByHour(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds) {

        return salesReportService.salesByHour(
                buildParams(from, to, storeIds, null, null, null, null));
    }

    @Tool(description = """
            Get sales broken down by weekday (Monday–Sunday).
            """)
    public List<SalesByWeekdayDto> getSalesByWeekday(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds) {

        return salesReportService.salesByWeekday(
                buildParams(from, to, storeIds, null, null, null, null));
    }

    @Tool(description = """
            Get average basket / basket analysis metrics.
            Returns average basket value, items per basket, discount rates, etc.
            """)
    public AverageBasketDto getAverageBasket(
            @ToolParam(description = "Start date (yyyy-MM-dd)", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd)", required = false) LocalDate to,
            @ToolParam(description = "Filter by store IDs", required = false) List<UUID> storeIds) {

        return salesReportService.averageBasket(
                buildParams(from, to, storeIds, null, null, null, null));
    }
}
