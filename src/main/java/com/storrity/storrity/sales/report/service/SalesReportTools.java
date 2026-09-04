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
            List<String> productCodes,
            List<String> productCategories,
            List<String> performedBy,
            String sort,
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
        if (productCodes != null && !productCodes.isEmpty()) {
            params.setProductCodes(productCodes);
        }
        if (productCategories != null && !productCategories.isEmpty()) {
            params.setProductCategories(productCategories);
        }
        if (performedBy != null && !performedBy.isEmpty()) {
            params.setPerformedBy(performedBy);
        }
        if (sort != null) {
            params.setSort(sort);
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
            Get hourly sales summary.
            Returns a list of hourly aggregates, one row per distinct (date, hour) combination in the requested
            range that had at least one sale, sorted by date then hour ascending. Unlike getSalesByHour, this does
            NOT collapse hours across days — each day's hours are kept separate, so this is the finer-grained
            sibling of getDailySalesSummary rather than a peak-hour distribution tool.
            Best for detailed hour-by-hour performance within a specific day or short range; for aggregating a
            recurring peak-hour pattern across many days use getSalesByHour instead.

            Each row contains:
            - hour: hour of day, 0-23 (0 = midnight-1am, 23 = 11pm-midnight)
            - date (yyyy-MM-dd): the calendar day this hour belongs to
            - transactions: count of transactions completed during this hour on this date
            - quantitySold: total units sold during this hour on this date
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied during this hour
            - tax: total tax collected during this hour
            - netSales: final sales value after discount and tax adjustments

            If no rows match the filters, an empty list is returned.
            """)
    public List<HourlySalesSummaryDto> getHourlySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "date:asc,hour:asc" or "netSales:desc,quantitySold:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.hourlySalesSummary(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
        Get daily sales summary.
        Returns a list of daily aggregates, one row per calendar day in the requested range, sorted by date ascending.
        Best for short periods or detailed day-by-day performance; for longer ranges use a coarser-grained summary tool instead.

        Each row contains:
        - date (yyyy-MM-dd): the calendar day
        - transactions: count of completed transactions that day
        - quantitySold: total units sold across all line items
        - grossSales: total sales value before discounts and tax deductions
        - discount: total discount amount applied
        - tax: total tax collected (in Naira)
        - netSales: final sales value after discount and tax adjustments, i.e. what the business actually realized

        If no rows match the filters, an empty list is returned.
        """)
    public List<DailySalesSummaryDto> getDailySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "date:asc" or "quantitySold:asc" or "netSales:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {
        return salesReportService.dailySalesSummary(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get weekly sales summary.
            Returns a list of weekly aggregates, one row per calendar week in the requested range, sorted by week ascending.
            Best for medium-length trend charts and week-over-week comparisons; use the daily summary for finer granularity
            or the monthly/quarterly/yearly summaries for longer-range trends.

            Each row contains:
            - reportingWeekStartDate (yyyy-MM-dd): the date of the first day of the reporting week
            - reportingYear: the calendar year the week belongs to
            - reportingWeek: the week number within reportingYear (1-52/53)
            - transactions: count of completed transactions that week
            - quantitySold: total units sold across all line items
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied that week
            - tax: total tax collected that week
            - netSales: final sales value after discount and tax adjustments

            If no rows match the filters, an empty list is returned.
            """)
    public List<WeeklySalesSummaryDto> getWeeklySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "quantitySold:asc" or "reportingYear:desc,reportingWeek:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.weeklySalesSummary(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get monthly sales summary.
            Returns a list of monthly aggregates, one row per calendar month in the requested range, sorted by month ascending.
            Best tool for monthly trend charts and period-over-period comparisons.

            Each row contains:
            - reportingMonthStartDate (yyyy-MM-dd): the first day of the reporting month
            - reportingYear: the calendar year the month belongs to
            - reportingMonth: the month number within reportingYear (1-12)
            - transactions: count of completed transactions that month
            - quantitySold: total units sold across all line items
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied that month
            - tax: total tax collected that month
            - netSales: final sales value after discount and tax adjustments

            If no rows match the filters, an empty list is returned.
            """)
    public List<MonthlySalesSummaryDto> getMonthlySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "quantitySold:desc" or "reportingMonthStartDate:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.monthlySalesSummary(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get quarterly sales summary.
            Returns a list of quarterly aggregates, one row per calendar quarter in the requested range, sorted by quarter ascending.
            Useful for quarter-over-quarter trend charts and medium-to-long-range comparisons.

            Each row contains:
            - reportingQuarterStartDate (yyyy-MM-dd): the first day of the reporting quarter
            - reportingYear: the calendar year the quarter belongs to
            - reportingQuarter: the quarter number within reportingYear (1-4)
            - transactions: count of completed transactions that quarter
            - quantitySold: total units sold across all line items
            - grossSales: total sales value before discounts and tax deductions (in Naira)
            - discount: total discount amount applied that quarter (in Naira)
            - tax: total tax collected that quarter (in Naira)
            - netSales: final sales value after discount and tax adjustments (in Naira)

            If no rows match the filters, an empty list is returned.
            """)
    public List<QuarterlySalesSummaryDto> getQuarterlySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "grossSales:desc" or "reportingQuarterStartDate:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.quarterlySalesSummary(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get yearly sales summary.
            Returns a list of yearly aggregates, one row per calendar year in the requested range, sorted by year ascending.
            Useful for high-level yearly trends and long-range year-over-year comparisons.

            Each row contains:
            - reportingYear: the calendar year
            - transactions: count of completed transactions that year
            - quantitySold: total units sold across all line items
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied that year
            - tax: total tax collected that year
            - netSales: final sales value after discount and tax adjustments

            If no rows match the filters, an empty list is returned.
            """)
    public List<YearlySalesSummaryDto> getYearlySalesSummary(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "grossSales:desc" or "reportingYear:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.yearlySalesSummary(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    // =====================================================
    // Breakdowns
    // =====================================================

    @Tool(description = """
            Get sales broken down by store.
            Excellent for comparing store performance. Returns one row per store, sorted by netSales descending
            (highest-performing store first) unless otherwise filtered.

            Each row contains:
            - storeId: unique store identifier
            - storeName: display name of the store
            - transactions: count of transactions completed at this store
            - quantitySold: total units sold at this store
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied at this store
            - tax: total tax collected at this store
            - netSales: final sales value after discount and tax adjustments

            If no rows match the filters, an empty list is returned.
            """)
    public List<SalesByStoreDto> getSalesByStore(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "storeName:asc" or "quantitySold:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.salesByStore(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
        Get sales broken down by product.
        Returns one row per product, useful for top-selling products charts and product-level performance analysis.
        Rows are sorted by netSales descending (highest-performing product first) unless otherwise filtered.

        Each row contains:
        - productId: unique product identifier
        - productName: display name of the product
        - productCode: SKU or product code
        - productCategory: top-level category the product belongs to
        - productSubCategory: sub-category the product belongs to (may be null if uncategorized)
        - transactions: count of transactions that included this product
        - quantitySold: total units of this product sold
        - grossSales: total sales value before discounts and tax deductions
        - discount: total discount amount applied to this product
        - tax: total tax collected on this product
        - netSales: final sales value after discount and tax adjustments

        If no rows match the filters, an empty list is returned.
        """)
    public List<SalesByProductDto> getSalesByProduct(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "productName:asc" or "grossSales:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.salesByProduct(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get sales broken down by product category.
            Returns one row per product category, useful for comparing performance across categories.
            Rows are sorted by netSales descending (highest-performing category first) unless otherwise filtered.

            Each row contains:
            - productCategory: name of the category
            - transactions: count of transactions that included products in this category
            - quantitySold: total units sold across this category
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied within this category
            - tax: total tax collected within this category
            - netSales: final sales value after discount and tax adjustments

            If no rows match the filters, an empty list is returned.
            """)
    public List<SalesByCategoryDto> getSalesByCategory(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "productCategory:asc" or "grossSales:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.salesByCategory(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get sales broken down by brand.
            Returns one row per brand, useful for comparing performance across brands.
            Rows are sorted by netSales descending (highest-performing brand first) unless otherwise filtered.

            Each row contains:
            - brand: name of the brand
            - transactions: count of transactions that included products of this brand
            - quantitySold: total units sold across this brand
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied within this brand
            - tax: total tax collected within this brand
            - netSales: final sales value after discount and tax adjustments

            If no rows match the filters, an empty list is returned.
            """)
    public List<SalesByBrandDto> getSalesByBrand(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "brand:asc" or "grossSales:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.salesByBrand(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get sales broken down by cashier / sales attendant.
            Returns one row per cashier, useful for staff performance comparison.
            Rows are sorted by netSales descending (highest-performing cashier first) unless otherwise filtered.

            Each row contains:
            - cashier: name or identifier of the cashier/sales attendant
            - transactions: count of transactions handled by this cashier
            - quantitySold: total units sold by this cashier
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied on this cashier's sales
            - tax: total tax collected on this cashier's sales
            - netSales: final sales value after discount and tax adjustments

            If no rows match the filters, an empty list is returned.
            """)
    public List<SalesByCashierDto> getSalesByCashier(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "cashier:asc" or "grossSales:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.salesByCashier(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get sales broken down by customer.
            Useful for identifying top customers. Returns one row per customer, sorted by netSales descending
            (highest-spending customer first) unless otherwise filtered.

            Each row contains:
            - customerId: unique customer identifier
            - customerName: display name of the customer
            - transactions: count of transactions made by this customer
            - quantitySold: total units purchased by this customer
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied to this customer's purchases
            - tax: total tax collected on this customer's purchases
            - netSales: final sales value after discount and tax adjustments

            If no rows match the filters, an empty list is returned.
            """)
    public List<SalesByCustomerDto> getSalesByCustomer(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "customerName:asc" or "grossSales:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.salesByCustomer(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get sales broken down by hour of day (0-23).
            Useful for identifying peak hours. Returns up to 24 rows, one per hour, sorted by hour ascending.
            Hours are in the store's local time and aggregate across all days in the requested range
            (e.g. hour 14 sums every 2-3pm interval across the whole period, not just one day).

            Each row contains:
            - hour: hour of day, 0-23 (0 = midnight-1am, 23 = 11pm-midnight)
            - transactions: count of transactions completed during this hour across the range
            - quantitySold: total units sold during this hour across the range
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied during this hour
            - tax: total tax collected during this hour
            - netSales: final sales value after discount and tax adjustments

            Hours with zero sales may be omitted rather than returned as a zero-value row — check for missing hours
            when computing an average or full 24-hour distribution.
            """)
    public List<SalesByHourDto> getSalesByHour(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "hour:asc" or "grossSales:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.salesByHour(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }
    
    @Tool(description = """
            Get sales broken down by month of year (January-December).
            Returns up to 12 rows, one per calendar month, sorted month ascending. Aggregates across all matching
            months in the requested range regardless of year (e.g. "March" sums every March in the period, not
            just one year's March) — useful for identifying seasonal patterns.

            Each row contains:
            - month: month number, 1 (January) through 12 (December)
            - transactions: count of transactions completed in this month across the range
            - quantitySold: total units sold in this month across the range
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied in this month
            - tax: total tax collected in this month
            - netSales: final sales value after discount and tax adjustments

            Months with zero sales may be omitted rather than returned as a zero-value row — check for missing
            months when computing an average or full 12-month distribution.
            """)
    public List<SalesByMonthDto> getSalesByMonth(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        use propery in rows 
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "month:asc" or "grossSales:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.salesByMonth(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get sales broken down by weekday (Monday-Sunday).
            Returns up to 7 rows, one per weekday, sorted Monday to Sunday. Aggregates across all matching dates
            in the requested range (e.g. "Monday" sums every Monday in the period, not just one).

            Each row contains:
            - dayOfWeek: ISO-8601 day number, 1 (Monday) through 7 (Sunday)
            - dayName: full weekday name corresponding to dayOfWeek (e.g. "Monday")
            - transactions: count of transactions completed on this weekday across the range
            - quantitySold: total units sold on this weekday across the range
            - grossSales: total sales value before discounts and tax deductions
            - discount: total discount amount applied on this weekday
            - tax: total tax collected on this weekday
            - netSales: final sales value after discount and tax adjustments

            Weekdays with zero sales may be omitted rather than returned as a zero-value row.
            """)
    public List<SalesByWeekdayDto> getSalesByWeekday(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(
                description = """
                        Sort results using comma-separated property and direction pairs.
                        Format: property:direction.
                        Use 'asc' for ascending or 'desc' for descending.
                        use propery in rows 
                        Multiple sort fields are separated by commas and are applied in the specified order.
                        Example: "dayOfWeek:asc" or "grossSales:desc".
                        Omit to use the repository's default sorting.
                        """,
                required = false)
            String sort,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.salesByWeekday(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, sort, limit));
    }

    @Tool(description = """
            Get average basket / basket analysis metrics.
            Returns a single summary object (not a list) aggregating all baskets (transactions) matching the filters
            over the requested period — useful for understanding typical purchase size, discount penetration,
            and buying patterns, as opposed to time-series or per-entity breakdowns.

            The response contains:

            Basket counts and totals:
            - totalBaskets: number of completed baskets (distinct transactions) in the period
            - totalRevenue: sum of net revenue across all baskets (in Naira)
            - totalItemsSold: total quantity of items sold across all baskets

            Basket value distribution (in Naira):
            - averageBasketValue: mean basket value (total revenue / total baskets)
            - medianBasketValue: median basket value; more robust than the average since it's less skewed
              by unusually large baskets
            - smallestBasketValue: value of the smallest basket in the period
            - largestBasketValue: value of the largest basket in the period
            - averageGrossBasketValue: mean basket value before discounts are applied
            - averageDiscountPerBasket: mean discount amount applied per basket
            - averageTaxPerBasket: mean tax amount collected per basket

            Basket composition (item counts, not currency):
            - averageItemsPerBasket: mean total quantity of items purchased per basket
            - averageUniqueProductsPerBasket: mean number of distinct products (SKUs) per basket
            - averageUnitsPerProduct: mean quantity per unique product within a basket (averageItemsPerBasket /
              averageUniqueProductsPerBasket); indicates whether customers tend to buy multiples of the same product
              rather than one of many different products

            Discount penetration:
            - discountedBasketCount: number of baskets that received at least one discount
            - discountedBasketPercentage: percentage of all baskets (0-100) that received a discount
            - averageDiscountRate: mean discount rate applied, as a percentage (0-100) of gross basket value

            If no baskets match the filters, fields will reflect zero/empty results (e.g. totalBaskets = 0)
            rather than an empty list, since this tool always returns exactly one object.
            """)
    public AverageBasketDto getAverageBasket(
            @ToolParam(description = "Start date (yyyy-MM-dd), inclusive. Defaults to earliest available data if omitted.", required = false) LocalDate from,
            @ToolParam(description = "End date (yyyy-MM-dd), inclusive. Defaults to today if omitted.", required = false) LocalDate to,
            @ToolParam(description = "Filter to specific store IDs. Omit to include all stores.", required = false) List<UUID> storeIds,
            @ToolParam(description = "Filter to specific product IDs. Omit to include all products.", required = false) List<UUID> productIds,
            @ToolParam(description = "Filter to specific product codes. Omit to include all codes.", required = false) List<String> productCodes,
            @ToolParam(description = "Filter to specific product categories. Omit to include all categories.", required = false) List<String> productCategories,
            @ToolParam(description = "Filter to specific cashiers/staff who performed the sale. Omit to include all.", required = false) List<String> performedBy,
            @ToolParam(description = "Maximum number of daily rows to return (most recent first if truncated). Omit for no limit.", required = false) Integer limit) {

        return salesReportService.averageBasket(
                buildParams(from, to, storeIds, productIds, productCodes, productCategories, performedBy, null, limit));
    }
}
