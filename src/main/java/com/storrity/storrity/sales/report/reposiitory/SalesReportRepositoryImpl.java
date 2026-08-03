/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.reposiitory;

import com.storrity.storrity.sales.report.dto.DailySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.MonthlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.QuarterlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.SalesReportQueryParams;
import com.storrity.storrity.sales.report.dto.WeeklySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.YearlySalesSummaryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Seun Owa
 */
@Repository
public class SalesReportRepositoryImpl implements SalesReportRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<DailySalesSummaryDto> dailySalesSummary(
            SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                reporting_date,
                COUNT(*)                               AS transactions,
                SUM(quantity)                          AS quantity_sold,
                SUM(pre_discount_price_in_micro_naira) AS gross_sales,
                SUM(discount_amount_in_micro_naira)    AS discount,
                SUM(tax_amount_in_micro_naira)         AS tax,
                SUM(amount_in_micro_naira)             AS net_sales
            FROM sale
            WHERE 1 = 1
            """);

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY reporting_date
            ORDER BY reporting_date
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        if (params.getOffset() != null) {
            query.setFirstResult(params.getOffset());
        }

        if (params.getLimit() != null) {
            query.setMaxResults(params.getLimit());
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<DailySalesSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            result.add(new DailySalesSummaryDto(
                    (java.sql.Date)row[0],
                    ((Number) row[1]).longValue(),
                    row[2] == null ? 0d : ((Number) row[2]).doubleValue(),
                    row[3] == null ? 0L : ((Number) row[3]).longValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue()
            ));
        }

        return result;
    }

    @Override
    public List<WeeklySalesSummaryDto> weeklySalesSummary(SalesReportQueryParams params) {
        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                reporting_week_start_date,
                reporting_year,
                reporting_week,
                COUNT(*),
                SUM(quantity),
                SUM(pre_discount_price_in_micro_naira),
                SUM(discount_amount_in_micro_naira),
                SUM(tax_amount_in_micro_naira),
                SUM(amount_in_micro_naira)
            FROM sale
            WHERE 1 = 1
            """);

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                reporting_week_start_date,
                reporting_year,
                reporting_week
            ORDER BY
                reporting_week_start_date;
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        if (params.getOffset() != null) {
            query.setFirstResult(params.getOffset());
        }

        if (params.getLimit() != null) {
            query.setMaxResults(params.getLimit());
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<WeeklySalesSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            result.add(new WeeklySalesSummaryDto(
                    (java.sql.Date)row[0],                    
                    ((Number) row[1]).intValue(),
                    ((Number) row[2]).intValue(),
                    ((Number) row[3]).longValue(),
                    row[4] == null ? 0d : ((Number) row[4]).doubleValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue(),
                    row[7] == null ? 0L : ((Number) row[7]).longValue(),
                    row[8] == null ? 0L : ((Number) row[8]).longValue()
            ));
        }

        return result;
    }

    @Override
    public List<MonthlySalesSummaryDto> monthlySalesSummary(SalesReportQueryParams params) {
        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                reporting_month_start_date,
                reporting_year,
                reporting_month,
                COUNT(*),
                SUM(quantity),
                SUM(pre_discount_price_in_micro_naira),
                SUM(discount_amount_in_micro_naira),
                SUM(tax_amount_in_micro_naira),
                SUM(amount_in_micro_naira)
            FROM sale
            WHERE 1 = 1
            """);

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                reporting_month_start_date,
                reporting_year,
                reporting_month
            ORDER BY
                reporting_month_start_date;
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        if (params.getOffset() != null) {
            query.setFirstResult(params.getOffset());
        }

        if (params.getLimit() != null) {
            query.setMaxResults(params.getLimit());
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<MonthlySalesSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            result.add(new MonthlySalesSummaryDto(
                    (java.sql.Date)row[0],                    
                    ((Number) row[1]).intValue(),
                    ((Number) row[2]).intValue(),
                    ((Number) row[3]).longValue(),
                    row[4] == null ? 0d : ((Number) row[4]).doubleValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue(),
                    row[7] == null ? 0L : ((Number) row[7]).longValue(),
                    row[8] == null ? 0L : ((Number) row[8]).longValue()
            ));
        }

        return result;
    }

    @Override
    public List<QuarterlySalesSummaryDto> quarterlySalesSummary(SalesReportQueryParams params) {
        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                reporting_quarter_start_date,
                reporting_year,
                reporting_quarter,
                COUNT(*),
                SUM(quantity),
                SUM(pre_discount_price_in_micro_naira),
                SUM(discount_amount_in_micro_naira),
                SUM(tax_amount_in_micro_naira),
                SUM(amount_in_micro_naira)
            FROM sale
            WHERE 1 = 1
            """);

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                reporting_quarter_start_date,
                reporting_year,
                reporting_quarter
            ORDER BY
                reporting_quarter_start_date;
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        if (params.getOffset() != null) {
            query.setFirstResult(params.getOffset());
        }

        if (params.getLimit() != null) {
            query.setMaxResults(params.getLimit());
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<QuarterlySalesSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            result.add(new QuarterlySalesSummaryDto(
                    (java.sql.Date)row[0],                    
                    ((Number) row[1]).intValue(),
                    ((Number) row[2]).intValue(),
                    ((Number) row[3]).longValue(),
                    row[4] == null ? 0d : ((Number) row[4]).doubleValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue(),
                    row[7] == null ? 0L : ((Number) row[7]).longValue(),
                    row[8] == null ? 0L : ((Number) row[8]).longValue()
            ));
        }

        return result;
    }

    @Override
    public List<YearlySalesSummaryDto> yearlySalesSummary(SalesReportQueryParams params) {
        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                reporting_year,
                COUNT(*),
                SUM(quantity),
                SUM(pre_discount_price_in_micro_naira),
                SUM(discount_amount_in_micro_naira),
                SUM(tax_amount_in_micro_naira),
                SUM(amount_in_micro_naira)
            FROM sale
            WHERE 1 = 1
            """);

        appendFilters(sql, parameters, params);

        sql.append("""
            GROUP BY
                reporting_quarter_start_date,
                reporting_year,
                reporting_quarter
            ORDER BY
                reporting_quarter_start_date;
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        if (params.getOffset() != null) {
            query.setFirstResult(params.getOffset());
        }

        if (params.getLimit() != null) {
            query.setMaxResults(params.getLimit());
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<YearlySalesSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            result.add(new YearlySalesSummaryDto(            
                    ((Number) row[0]).intValue(),
                    ((Number) row[1]).longValue(),
                    row[2] == null ? 0d : ((Number) row[2]).doubleValue(),
                    row[3] == null ? 0L : ((Number) row[3]).longValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue()
            ));
        }

        return result;
    }
    
    

    private void appendFilters(
            StringBuilder sql,
            Map<String, Object> parameters,
            SalesReportQueryParams params) {

        if (params.getStoreIds() != null && !params.getStoreIds().isEmpty()) {
            sql.append(" AND store_id IN (:storeIds)");
            parameters.put("storeIds", params.getStoreIds());
        }

        if (params.getProductIds() != null && !params.getProductIds().isEmpty()) {
            sql.append(" AND product_id IN (:productIds)");
            parameters.put("productIds", params.getProductIds());
        }

        if (params.getCustomerIds() != null && !params.getCustomerIds().isEmpty()) {
            sql.append(" AND customer_id IN (:customerIds)");
            parameters.put("customerIds", params.getCustomerIds());
        }

        if (params.getProductCategories() != null && !params.getProductCategories().isEmpty()) {
            sql.append(" AND product_category IN (:productCategories)");
            parameters.put("productCategories", params.getProductCategories());
        }

        if (params.getProductSubCategories() != null && !params.getProductSubCategories().isEmpty()) {
            sql.append(" AND product_sub_category IN (:productSubCategories)");
            parameters.put("productSubCategories", params.getProductSubCategories());
        }

        if (params.getPerformedBy() != null && !params.getPerformedBy().isEmpty()) {
            sql.append(" AND performed_by IN (:performedBy)");
            parameters.put("performedBy", params.getPerformedBy());
        }

        if (params.getClientSystemIds() != null && !params.getClientSystemIds().isEmpty()) {
            sql.append(" AND client_system_id IN (:clientSystemIds)");
            parameters.put("clientSystemIds", params.getClientSystemIds());
        }

        if (params.getCreatedAtRange() != null
                && params.getCreatedAtRange().size() == 2) {

            sql.append("""
                AND created_at BETWEEN :fromDate
                                   AND :toDate
            """);

            parameters.put("fromDate", params.getCreatedAtRange().get(0));
            parameters.put("toDate", params.getCreatedAtRange().get(1));
        }
    }
}