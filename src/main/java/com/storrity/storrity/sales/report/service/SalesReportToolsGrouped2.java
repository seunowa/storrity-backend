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
@Deprecated
//@Component
public class SalesReportToolsGrouped2 {

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

    public SalesReportToolsGrouped2(SalesReportService salesReportService) {
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
            Returns a sales summary aggregated over a time period. Choose this tool whenever the user asks about:
            sales today, sales yesterday, daily sales, weekly sales, monthly sales, quarterly sales, yearly sales,
            revenue trend, sales trend, or historical sales performance.

            The summaryPeriod parameter determines the level of aggregation and, correspondingly, the shape of
            each row in the returned list. In every case, all monetary fields (grossSales, discount, tax, netSales)
            are expressed in Naira, and netSales = grossSales - discount - tax adjustments (the figure the business
            actually realized). Rows are sorted ascending by their time bucket (earliest first) unless truncated by limit.

            summaryPeriod = DAILY -> one row per calendar day:
            - date (yyyy-MM-dd): the calendar day
            - transactions: count of completed transactions that day
            - quantitySold: total units sold
            - grossSales, discount, tax, netSales: as defined above, for that day

            summaryPeriod = WEEKLY -> one row per calendar week:
            - reportingWeekStartDate (yyyy-MM-dd): first day of the reporting week
            - reportingYear: calendar year the week belongs to
            - reportingWeek: week number within reportingYear (1-52/53, ISO-8601)
            - transactions: count of completed transactions that week
            - quantitySold: total units sold
            - grossSales, discount, tax, netSales: as defined above, for that week

            summaryPeriod = MONTHLY -> one row per calendar month:
            - reportingMonthStartDate (yyyy-MM-dd): first day of the reporting month
            - reportingYear: calendar year the month belongs to
            - reportingMonth: month number within reportingYear (1-12)
            - transactions: count of completed transactions that month
            - quantitySold: total units sold
            - grossSales, discount, tax, netSales: as defined above, for that month

            summaryPeriod = QUARTERLY -> one row per calendar quarter:
            - reportingQuarterStartDate (yyyy-MM-dd): first day of the reporting quarter
            - reportingYear: calendar year the quarter belongs to
            - reportingQuarter: quarter number within reportingYear (1-4)
            - transactions: count of completed transactions that quarter
            - quantitySold: total units sold
            - grossSales, discount, tax, netSales: as defined above, for that quarter

            summaryPeriod = YEARLY -> one row per calendar year:
            - reportingYear: the calendar year
            - transactions: count of completed transactions that year
            - quantitySold: total units sold
            - grossSales, discount, tax, netSales: as defined above, for that year

            If no rows match the filters, an empty list is returned.
            """)
    public Object getSalesSummary(

            @ToolParam(description = "Time aggregation level: DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY. Determines the shape of each returned row — see tool description.") SalesSummaryPeriod summaryPeriod,
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(description = "Maximum number of rows to return (earliest-first order is truncated from the end, i.e. you get the earliest N periods). Omit for no limit.", required = false) Integer limit) {

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
            Returns sales grouped by a business dimension. Choose this tool whenever the user asks questions like:
            sales by store, top stores, best performing stores; sales by product, top products; sales by category;
            sales by brand; sales by cashier, best cashier; sales by customer, top customers; busiest hours,
            peak sales hours; busiest weekdays.

            The breakdownType parameter determines how sales are grouped and, correspondingly, the shape of each
            row in the returned list. In every case, all monetary fields (grossSales, discount, tax, netSales) are
            expressed in Naira, and netSales = grossSales - discount - tax adjustments. Unless otherwise noted,
            rows are sorted by netSales descending (best performer first).

            breakdownType = STORE -> one row per store:
            - storeId: unique store identifier
            - storeName: display name of the store
            - transactions: count of transactions completed at this store
            - quantitySold: total units sold at this store
            - grossSales, discount, tax, netSales: as defined above, for that store

            breakdownType = PRODUCT -> one row per product:
            - productId: unique product identifier
            - productName: display name of the product
            - productCode: SKU or product code
            - productCategory: top-level category the product belongs to
            - productSubCategory: sub-category the product belongs to (may be null if uncategorized)
            - transactions: count of transactions that included this product
            - quantitySold: total units of this product sold
            - grossSales, discount, tax, netSales: as defined above, for that product

            breakdownType = CATEGORY -> one row per product category:
            - productCategory: name of the category
            - transactions: count of transactions that included products in this category
            - quantitySold: total units sold across this category
            - grossSales, discount, tax, netSales: as defined above, for that category

            breakdownType = BRAND -> one row per brand:
            - brand: name of the brand
            - transactions: count of transactions that included products of this brand
            - quantitySold: total units sold across this brand
            - grossSales, discount, tax, netSales: as defined above, for that brand

            breakdownType = CASHIER -> one row per cashier/sales attendant:
            - cashier: name or identifier of the cashier
            - transactions: count of transactions handled by this cashier
            - quantitySold: total units sold by this cashier
            - grossSales, discount, tax, netSales: as defined above, for that cashier

            breakdownType = CUSTOMER -> one row per customer:
            - customerId: unique customer identifier
            - customerName: display name of the customer
            - transactions: count of transactions made by this customer
            - quantitySold: total units purchased by this customer
            - grossSales, discount, tax, netSales: as defined above, for that customer

            breakdownType = HOUR -> up to 24 rows, one per hour of day, sorted by hour ascending (0-23, store-local
            time). Each row aggregates across every day in the requested range (e.g. hour 14 sums every 2-3pm
            interval across the whole period). Hours with zero sales may be omitted rather than returned as a
            zero-value row:
            - hour: hour of day, 0-23 (0 = midnight-1am, 23 = 11pm-midnight)
            - transactions: count of transactions completed during this hour across the range
            - quantitySold: total units sold during this hour across the range
            - grossSales, discount, tax, netSales: as defined above, for that hour bucket

            breakdownType = WEEKDAY -> up to 7 rows, one per weekday, sorted Monday to Sunday. Each row aggregates
            across every matching date in the requested range (e.g. "Monday" sums every Monday in the period).
            Weekdays with zero sales may be omitted rather than returned as a zero-value row:
            - dayOfWeek: ISO-8601 day number, 1 (Monday) through 7 (Sunday)
            - dayName: full weekday name corresponding to dayOfWeek (e.g. "Monday")
            - transactions: count of transactions completed on this weekday across the range
            - quantitySold: total units sold on this weekday across the range
            - grossSales, discount, tax, netSales: as defined above, for that weekday bucket

            If no rows match the filters, an empty list is returned.
            """)
    public Object getSalesBreakdown(
            @ToolParam(description = "Breakdown dimension: STORE, PRODUCT, CATEGORY, BRAND, CASHIER, CUSTOMER, HOUR, WEEKDAY. Determines the shape of each returned row — see tool description.") SalesBreakdownType breakdownType,
            @ToolParam( description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam( description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam( description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam( description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam( description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam( description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam( description = "Maximum number of rows to return (highest netSales first if truncated, except for HOUR/WEEKDAY which are always time-ordered). Omit for no limit.", required = false) Integer limit) {

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
            Returns overall basket analysis metrics. Choose this tool whenever the user asks: average basket,
            average basket value, average order value, average transaction value, basket statistics, basket
            analysis, customer buying behaviour, purchase behaviour, average items purchased, or discount
            penetration.

            Returns a single AverageBasketDto object (not a list) aggregating all baskets (transactions) matching
            the filters over the requested period. If no baskets match, fields reflect zero/empty results
            (e.g. totalBaskets = 0) rather than an empty list, since exactly one object is always returned.

            The response contains:

            Basket counts and totals:
            - totalBaskets: number of completed baskets (distinct transactions) in the period
            - totalRevenue: sum of net revenue across all baskets (in Naira)
            - totalItemsSold: total quantity of items sold across all baskets

            Basket value distribution (in Naira):
            - averageBasketValue: mean basket value (total revenue / total baskets)
            - medianBasketValue: median basket value; more robust than the average since it's less skewed by
              unusually large baskets
            - smallestBasketValue: value of the smallest basket in the period
            - largestBasketValue: value of the largest basket in the period
            - averageGrossBasketValue: mean basket value before discounts are applied
            - averageDiscountPerBasket: mean discount amount applied per basket
            - averageTaxPerBasket: mean tax amount collected per basket

            Basket composition (item counts, not currency):
            - averageItemsPerBasket: mean total quantity of items purchased per basket
            - averageUniqueProductsPerBasket: mean number of distinct products (SKUs) per basket
            - averageUnitsPerProduct: mean quantity per unique product within a basket; indicates whether
              customers tend to buy multiples of the same product rather than one of many different products

            Discount penetration:
            - discountedBasketCount: number of baskets that received at least one discount
            - discountedBasketPercentage: percentage of all baskets (0-100) that received a discount
            - averageDiscountRate: mean discount rate applied, as a percentage (0-100) of gross basket value
            """)
    public AverageBasketDto getAverageBasket(
            @ToolParam( description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam( description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam( description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam( description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam( description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam( description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam( description = "Not applicable to this tool since it returns a single aggregate object rather than a list of rows; any value passed is ignored.", required = false) Integer limit) {

        SalesReportQueryParams params = buildParams( from, to, storeIds, productIds, productCategories, performedBy, limit);
        return salesReportService.averageBasket(params);
    }

}