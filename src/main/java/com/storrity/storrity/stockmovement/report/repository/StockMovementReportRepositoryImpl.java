/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stockmovement.report.repository;

import com.storrity.storrity.stockmovement.entity.StockMoevmentDirection;
import com.storrity.storrity.stockmovement.entity.StockMovementType;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Seun Owa
 */
@Repository
public class StockMovementReportRepositoryImpl
        implements StockMovementReportRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private Environment environment;

    @Override
    public List<HourlyStockMovementSummaryDto> hourlyStockMovementSummary(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                reporting_hour,
                reporting_date,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                reporting_date,
                reporting_hour,
                base_unit
            ORDER BY
                reporting_date,
                reporting_hour,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<HourlyStockMovementSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new HourlyStockMovementSummaryDto(
                    getInt(row[0]),
                    (java.sql.Date) row[1],
                    getLong(row[2]),
                    getDouble(row[3]),
                    getDouble(row[4]),
                    getDouble(row[5]),
                    getLong(row[6]),
                    getLong(row[7]),
                    getLong(row[8]),
                    (String) row[9]
            ));
        }

        return result;
    }

    @Override
    public List<DailyStockMovementSummaryDto> dailyStockMovementSummary(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                reporting_date,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                reporting_date,
                base_unit
            ORDER BY
                reporting_date,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<DailyStockMovementSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new DailyStockMovementSummaryDto(
                    (java.sql.Date) row[0],
                    getLong(row[1]),
                    getDouble(row[2]),
                    getDouble(row[3]),
                    getDouble(row[4]),
                    getLong(row[5]),
                    getLong(row[6]),
                    getLong(row[7]),
                    (String) row[8]
            ));
        }

        return result;
    }

    @Override
    public List<WeeklyStockMovementSummaryDto> weeklyStockMovementSummary(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                reporting_week_start_date,
                reporting_year,
                reporting_week,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                reporting_week_start_date,
                reporting_year,
                reporting_week,
                base_unit
            ORDER BY
                reporting_week_start_date,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<WeeklyStockMovementSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new WeeklyStockMovementSummaryDto(
                    (java.sql.Date) row[0],
                    getInt(row[1]),
                    getInt(row[2]),
                    getLong(row[3]),
                    getDouble(row[4]),
                    getDouble(row[5]),
                    getDouble(row[6]),
                    getLong(row[7]),
                    getLong(row[8]),
                    getLong(row[9]),
                    (String) row[10]
            ));
        }

        return result;
    }

    @Override
    public List<MonthlyStockMovementSummaryDto> monthlyStockMovementSummary(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                reporting_month_start_date,
                reporting_year,
                reporting_month,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                reporting_month_start_date,
                reporting_year,
                reporting_month,
                base_unit
            ORDER BY
                reporting_month_start_date,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<MonthlyStockMovementSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new MonthlyStockMovementSummaryDto(
                    (java.sql.Date) row[0],
                    getInt(row[1]),
                    getInt(row[2]),
                    getLong(row[3]),
                    getDouble(row[4]),
                    getDouble(row[5]),
                    getDouble(row[6]),
                    getLong(row[7]),
                    getLong(row[8]),
                    getLong(row[9]),
                    (String) row[10]
            ));
        }

        return result;
    }

    @Override
    public List<QuarterlyStockMovementSummaryDto> quarterlyStockMovementSummary(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                reporting_quarter_start_date,
                reporting_year,
                reporting_quarter,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                reporting_quarter_start_date,
                reporting_year,
                reporting_quarter,
                base_unit
            ORDER BY
                reporting_quarter_start_date,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<QuarterlyStockMovementSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new QuarterlyStockMovementSummaryDto(
                    (java.sql.Date) row[0],
                    getInt(row[1]),
                    getInt(row[2]),
                    getLong(row[3]),
                    getDouble(row[4]),
                    getDouble(row[5]),
                    getDouble(row[6]),
                    getLong(row[7]),
                    getLong(row[8]),
                    getLong(row[9]),
                    (String) row[10]
            ));
        }

        return result;
    }

    @Override
    public List<YearlyStockMovementSummaryDto> yearlyStockMovementSummary(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                reporting_year,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                reporting_year,
                base_unit
            ORDER BY
                reporting_year,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<YearlyStockMovementSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new YearlyStockMovementSummaryDto(
                    getInt(row[0]),
                    getLong(row[1]),
                    getDouble(row[2]),
                    getDouble(row[3]),
                    getDouble(row[4]),
                    getLong(row[5]),
                    getLong(row[6]),
                    getLong(row[7]),
                    (String) row[8]
            ));
        }

        return result;
    }

    @Override
    public List<StockMovementsByStoreDto> stockMovementsByStore(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                CAST(store_id AS VARCHAR),
                store_name,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                store_id,
                store_name,
                base_unit
            ORDER BY
                net_quantity DESC,
                store_name,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<StockMovementsByStoreDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new StockMovementsByStoreDto(
                    (String) row[0],
                    (String) row[1],
                    getLong(row[2]),
                    getDouble(row[3]),
                    getDouble(row[4]),
                    getDouble(row[5]),
                    getLong(row[6]),
                    getLong(row[7]),
                    getLong(row[8]),
                    (String) row[9]
            ));
        }

        return result;
    }

    @Override
    public List<StockMovementsByProductIdDto> stockMovementsByProductId(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                CAST(product_id AS VARCHAR),
                product_name,
                product_code,
                product_category,
                product_sub_category,
                product_brand,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                product_id,
                product_name,
                product_code,
                product_category,
                product_sub_category,
                product_brand,
                base_unit
            ORDER BY
                net_quantity DESC,
                product_name,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<StockMovementsByProductIdDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new StockMovementsByProductIdDto(
                    (String) row[0],
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    (String) row[4],
                    (String) row[5],
                    getLong(row[6]),
                    getDouble(row[7]),
                    getDouble(row[8]),
                    getDouble(row[9]),
                    getLong(row[10]),
                    getLong(row[11]),
                    getLong(row[12]),
                    (String) row[13]
            ));
        }

        return result;
    }

    @Override
    public List<StockMovementsByProductCodeDto> stockMovementsByProductCode(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                product_name,
                product_code,
                product_category,
                product_sub_category,
                product_brand,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                product_name,
                product_code,
                product_category,
                product_sub_category,
                product_brand,
                base_unit
            ORDER BY
                net_quantity DESC,
                product_name,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<StockMovementsByProductCodeDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new StockMovementsByProductCodeDto(
                    (String) row[0],
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    (String) row[4],
                    getLong(row[5]),
                    getDouble(row[6]),
                    getDouble(row[7]),
                    getDouble(row[8]),
                    getLong(row[9]),
                    getLong(row[10]),
                    getLong(row[11]),
                    (String) row[12]
            ));
        }

        return result;
    }

    @Override
    public List<StockMovementsByCategoryDto> stockMovementsByCategory(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                product_category,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                product_category,
                base_unit
            ORDER BY
                net_quantity DESC,
                product_category,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<StockMovementsByCategoryDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new StockMovementsByCategoryDto(
                    (String) row[0],
                    getLong(row[1]),
                    getDouble(row[2]),
                    getDouble(row[3]),
                    getDouble(row[4]),
                    getLong(row[5]),
                    getLong(row[6]),
                    getLong(row[7]),
                    (String) row[8]
            ));
        }

        return result;
    }

    @Override
    public List<StockMovementsByBrandDto> stockMovementsByBrand(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                product_brand,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                product_brand,
                base_unit
            ORDER BY
                net_quantity DESC,
                product_brand,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<StockMovementsByBrandDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new StockMovementsByBrandDto(
                    (String) row[0],
                    getLong(row[1]),
                    getDouble(row[2]),
                    getDouble(row[3]),
                    getDouble(row[4]),
                    getLong(row[5]),
                    getLong(row[6]),
                    getLong(row[7]),
                    (String) row[8]
            ));
        }

        return result;
    }

    @Override
    public List<StockMovementsByMovementTypeDto> stockMovementsByMovementType(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                movement_type,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                movement_type,
                base_unit
            ORDER BY
                movement_type,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<StockMovementsByMovementTypeDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new StockMovementsByMovementTypeDto(
                    StockMovementType.valueOf((String) row[0]),
                    getLong(row[1]),
                    getDouble(row[2]),
                    getDouble(row[3]),
                    getDouble(row[4]),
                    getLong(row[5]),
                    getLong(row[6]),
                    getLong(row[7]),
                    (String) row[8]
            ));
        }

        return result;
    }

    @Override
    public List<StockMovementsByDirectionDto> stockMovementsByDirection(
            StockMovementReportQueryParams params) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                direction,
            """);

        appendMetrics(sql);

        sql.append("""
                ,
                base_unit

            FROM stock_movement
            WHERE 1 = 1
            """);

        Map<String, Object> parameters = new HashMap<>();

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                direction,
                base_unit
            ORDER BY
                direction,
                base_unit
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        applyPagination(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<StockMovementsByDirectionDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new StockMovementsByDirectionDto(
                    StockMoevmentDirection.valueOf((String) row[0]),
                    getLong(row[1]),
                    getDouble(row[2]),
                    getDouble(row[3]),
                    getDouble(row[4]),
                    getLong(row[5]),
                    getLong(row[6]),
                    getLong(row[7]),
                    (String) row[8]
            ));
        }

        return result;
    }

    private void appendFilters(
            StringBuilder sql,
            Map<String, Object> parameters,
            StockMovementReportQueryParams params) {

        if (params.getStoreIds() != null
                && !params.getStoreIds().isEmpty()) {

            sql.append(" AND store_id IN (:storeIds)");
            parameters.put("storeIds", params.getStoreIds());
        }

        if (params.getProductIds() != null
                && !params.getProductIds().isEmpty()) {

            sql.append(" AND product_id IN (:productIds)");
            parameters.put("productIds", params.getProductIds());
        }

        if (params.getProductCodes() != null
                && !params.getProductCodes().isEmpty()) {

            sql.append(" AND product_code IN (:productCodes)");
            parameters.put("productCodes", params.getProductCodes());
        }

        if (params.getProductCategories() != null
                && !params.getProductCategories().isEmpty()) {

            sql.append(" AND product_category IN (:productCategories)");
            parameters.put(
                    "productCategories",
                    params.getProductCategories());
        }

        if (params.getProductSubCategories() != null
                && !params.getProductSubCategories().isEmpty()) {

            sql.append(" AND product_sub_category IN (:productSubCategories)");
            parameters.put(
                    "productSubCategories",
                    params.getProductSubCategories());
        }

        if (params.getProductBrands() != null
                && !params.getProductBrands().isEmpty()) {

            sql.append(" AND product_brand IN (:productBrands)");

            parameters.put(
                    "productBrands",
                    params.getProductBrands());
        }

        if (params.getMovementTypes() != null
                && !params.getMovementTypes().isEmpty()) {

            sql.append(" AND movement_type IN (:movementTypes)");

            parameters.put(
                    "movementTypes",
                    params.getMovementTypes()
                            .stream()
                            .map(Enum::name)
                            .toList());
        }

        if (params.getDirections() != null
                && !params.getDirections().isEmpty()) {

            sql.append(" AND direction IN (:directions)");

            parameters.put(
                    "directions",
                    params.getDirections()
                            .stream()
                            .map(Enum::name)
                            .toList());
        }

        if (params.getPerformedBy() != null
                && !params.getPerformedBy().isEmpty()) {

            sql.append(" AND performed_by IN (:performedBy)");
            parameters.put("performedBy", params.getPerformedBy());
        }

        if (params.getTransactionRefs() != null
                && !params.getTransactionRefs().isEmpty()) {

            sql.append(" AND transaction_ref IN (:transactionRefs)");

            parameters.put(
                    "transactionRefs",
                    params.getTransactionRefs());
        }

        if (params.getCreatedAtRange() != null
                && params.getCreatedAtRange().size() == 2) {

            sql.append("""
                AND created_at BETWEEN :fromDate AND :toDate
                """);

            parameters.put(
                    "fromDate",
                    params.getCreatedAtRange().get(0));

            parameters.put(
                    "toDate",
                    params.getCreatedAtRange().get(1));
        }
    }

    private void appendMetrics(StringBuilder sql) {

        sql.append("""
            COUNT(*) AS movements,

            COALESCE(
                SUM(qty_in),
                0
            ) AS quantity_in,

            COALESCE(
                SUM(qty_out),
                0
            ) AS quantity_out,

            COALESCE(
                SUM(qty_in - qty_out),
                0
            ) AS net_quantity,

            COALESCE(
                SUM(
                    CASE
                        WHEN direction = 'INFLOW'
                        THEN movement_value_in_micro_naira
                        ELSE 0
                    END
                ),
                0
            ) AS value_in,

            COALESCE(
                SUM(
                    CASE
                        WHEN direction = 'OUTFLOW'
                        THEN movement_value_in_micro_naira
                        ELSE 0
                    END
                ),
                0
            ) AS value_out,

            COALESCE(
                SUM(
                    CASE
                        WHEN direction = 'INFLOW'
                        THEN movement_value_in_micro_naira
                        ELSE -movement_value_in_micro_naira
                    END
                ),
                0
            ) AS net_value
            """);
    }

    private int getInt(Object value) {

        if (value == null) {
            return 0;
        }

        return ((Number) value).intValue();
    }

    private long getLong(Object value) {

        if (value == null) {
            return 0L;
        }

        return ((Number) value).longValue();
    }

    private double getDouble(Object value) {

        if (value == null) {
            return 0D;
        }

        return ((Number) value).doubleValue();
    }

    private void applyPagination(
            Query query,
            StockMovementReportQueryParams params) {

        if (params.getOffset() != null) {
            query.setFirstResult(params.getOffset());
        }

        if (params.getLimit() != null) {
            query.setMaxResults(params.getLimit());
        }
    }
}