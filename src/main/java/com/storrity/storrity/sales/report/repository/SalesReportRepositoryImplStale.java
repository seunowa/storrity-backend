/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.repository;

import com.storrity.storrity.cashaccounts.entity.Money;
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
//@Repository
public class SalesReportRepositoryImplStale implements SalesReportRepository {

    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private Environment environment;
    
    @Override
    public List<HourlySalesSummaryDto> hourlySalesSummary(SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                reporting_hour,
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
            GROUP BY
                reporting_date,
                reporting_hour
            ORDER BY
                reporting_date,
                reporting_hour
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

        List<HourlySalesSummaryDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new HourlySalesSummaryDto(
                    row[0] == null ? 0 : ((Number) row[0]).intValue(),
                    (java.sql.Date) row[1],
                    ((Number) row[2]).longValue(),
                    row[3] == null ? 0D : ((Number) row[3]).doubleValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue(),
                    row[7] == null ? 0L : ((Number) row[7]).longValue()
            ));
        }

        return result;
    }

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

    @Override
    public List<SalesByStoreDto> salesByStore(
            SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                CAST(store_id AS VARCHAR) AS store_id,
                store_name,
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
            GROUP BY
                store_id,
                store_name
            ORDER BY
                SUM(amount_in_micro_naira) DESC,
                store_name
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

        List<SalesByStoreDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new SalesByStoreDto(
                    (String) row[0],
                    (String) row[1],
                    ((Number) row[2]).longValue(),
                    row[3] == null ? 0d : ((Number) row[3]).doubleValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue(),
                    row[7] == null ? 0L : ((Number) row[7]).longValue()
            ));
        }

        return result;
    }
    
    @Override
    public List<SalesByProductDto> salesByProduct(
            SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                CAST(product_id AS VARCHAR) AS product_id,
                product_name,
                product_code,
                product_category,
                product_sub_category,
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
            GROUP BY
                product_id,
                product_name,
                product_code,
                product_category,
                product_sub_category
            ORDER BY
                SUM(amount_in_micro_naira) DESC,
                product_name
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

        List<SalesByProductDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new SalesByProductDto(
                    (String) row[0],
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    (String) row[4],
                    ((Number) row[5]).longValue(),
                    row[6] == null ? 0d : ((Number) row[6]).doubleValue(),
                    row[7] == null ? 0L : ((Number) row[7]).longValue(),
                    row[8] == null ? 0L : ((Number) row[8]).longValue(),
                    row[9] == null ? 0L : ((Number) row[9]).longValue(),
                    row[10] == null ? 0L : ((Number) row[10]).longValue()
            ));
        }

        return result;
    }
    
    @Override
    public List<SalesByCategoryDto> salesByCategory(
            SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                product_category,
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
            GROUP BY
                product_category
            ORDER BY
                SUM(amount_in_micro_naira) DESC,
                product_category
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

        List<SalesByCategoryDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new SalesByCategoryDto(
                    (String) row[0],
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
    public List<SalesByBrandDto> salesByBrand(
            SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                product_brand,
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
            GROUP BY product_brand
            ORDER BY product_brand
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

        List<SalesByBrandDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new SalesByBrandDto(
                    (String) row[0],
                    ((Number) row[1]).longValue(),
                    row[2] == null ? 0D : ((Number) row[2]).doubleValue(),
                    row[3] == null ? 0L : ((Number) row[3]).longValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue()
            ));
        }

        return result;
    }
    
    @Override
    public List<SalesByCashierDto> salesByCashier(SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                performed_by,
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
            GROUP BY performed_by
            ORDER BY SUM(amount_in_micro_naira) DESC
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

        List<SalesByCashierDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new SalesByCashierDto(
                    (String) row[0],                                    // cashier
                    ((Number) row[1]).longValue(),                      // transactions
                    row[2] == null ? 0D : ((Number) row[2]).doubleValue(),
                    row[3] == null ? 0L : ((Number) row[3]).longValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue()
            ));
        }

        return result;
    }

    @Override
    public List<SalesByClientSystemDto> salesByClientSystem(SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                client_system_id,
                client_system_name,
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
            GROUP BY
                client_system_id,
                client_system_name
            ORDER BY
                SUM(amount_in_micro_naira) DESC,
                client_system_name
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

        List<SalesByClientSystemDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new SalesByClientSystemDto(
                    (String) row[0],                                    // clientSystemId
                    (String) row[1],                                    // clientSystemName
                    ((Number) row[2]).longValue(),                      // transactions
                    row[3] == null ? 0D : ((Number) row[3]).doubleValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue(),
                    row[7] == null ? 0L : ((Number) row[7]).longValue()
            ));
        }

        return result;
    }
    
    @Override
    public List<SalesByCustomerDto> salesByCustomer(SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                CAST(customer_id AS VARCHAR) AS customer_id,
                customer_name,
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
            GROUP BY
                customer_id,
                customer_name
            ORDER BY
                SUM(amount_in_micro_naira) DESC,
                customer_name
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

        List<SalesByCustomerDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new SalesByCustomerDto(
                    (String)row[0],           // customerId
                    (String) row[1],                                     // customerName
                    ((Number) row[2]).longValue(),                       // transactions
                    row[3] == null ? 0D : ((Number) row[3]).doubleValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue(),
                    row[7] == null ? 0L : ((Number) row[7]).longValue()
            ));
        }

        return result;
    }
    
    @Override
    public List<SalesByHourDto> salesByHour(SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                reporting_hour,
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
            GROUP BY
                reporting_hour
            ORDER BY
                reporting_hour
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

        List<SalesByHourDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new SalesByHourDto(
                    row[0] == null ? 0 : ((Number) row[0]).intValue(),
                    ((Number) row[1]).longValue(),
                    row[2] == null ? 0D : ((Number) row[2]).doubleValue(),
                    row[3] == null ? 0L : ((Number) row[3]).longValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue()
            ));
        }

        return result;
    }
    
    @Override
    public List<SalesByMonthDto> salesByMonth(SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                reporting_month,
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
            GROUP BY
                reporting_month
            ORDER BY
                reporting_month
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

        List<SalesByMonthDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new SalesByMonthDto(
                    row[0] == null ? 0 : ((Number) row[0]).intValue(),
                    ((Number) row[1]).longValue(),
                    row[2] == null ? 0D : ((Number) row[2]).doubleValue(),
                    row[3] == null ? 0L : ((Number) row[3]).longValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue()
            ));
        }

        return result;
    }
    
    @Override
    public List<SalesByWeekdayDto> salesByWeekday(SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                reporting_day_of_week,
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
            GROUP BY
                reporting_day_of_week
            ORDER BY
                reporting_day_of_week
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

        List<SalesByWeekdayDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            result.add(new SalesByWeekdayDto(
                    row[0] == null ? 0 : ((Number) row[0]).intValue(),
                    ((Number) row[1]).longValue(),
                    row[2] == null ? 0D : ((Number) row[2]).doubleValue(),
                    row[3] == null ? 0L : ((Number) row[3]).longValue(),
                    row[4] == null ? 0L : ((Number) row[4]).longValue(),
                    row[5] == null ? 0L : ((Number) row[5]).longValue(),
                    row[6] == null ? 0L : ((Number) row[6]).longValue()
            ));
        }

        return result;
    }
    
    @Override
    public AverageBasketDto averageBasket(SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                COUNT(*) AS total_baskets,

                SUM(net_sales) AS total_revenue,

                AVG(net_sales) AS average_basket_value,

                MIN(net_sales) AS smallest_basket,

                MAX(net_sales) AS largest_basket,

                AVG(gross_sales) AS average_gross,

                AVG(discount) AS average_discount,

                AVG(tax) AS average_tax,

                AVG(item_count) AS average_items,

                AVG(product_count) AS average_unique_products,

                AVG(
                    CASE
                        WHEN product_count = 0 THEN 0
                        ELSE item_count * 1.0 / product_count
                    END
                ) AS average_units_per_product,

                SUM(item_count) AS total_items,

                SUM(
                    CASE
                        WHEN discount > 0 THEN 1
                        ELSE 0
                    END
                ) AS discounted_baskets,

                CASE
                    WHEN COUNT(*) = 0 THEN 0
                    ELSE
                        (
                            SUM(
                                CASE
                                    WHEN discount > 0 THEN 1
                                    ELSE 0
                                END
                            ) * 100.0
                        ) / COUNT(*)
                END AS discount_percentage,

                AVG(discount_rate) AS average_discount_rate

            FROM
            (
                SELECT

                    transaction_ref,

                    SUM(quantity) AS item_count,

                    COUNT(DISTINCT product_id) AS product_count,

                    SUM(amount_in_micro_naira) AS net_sales,

                    SUM(pre_discount_price_in_micro_naira) AS gross_sales,

                    SUM(discount_amount_in_micro_naira) AS discount,

                    SUM(tax_amount_in_micro_naira) AS tax,

                    AVG(discount_rate) AS discount_rate

                FROM sale

                WHERE 1 = 1
            """);

        appendFilters(sql, parameters, params);

        sql.append("""
                GROUP BY transaction_ref
            ) basket
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        Object[] row = (Object[]) query.getSingleResult();

        AverageBasketDto dto = new AverageBasketDto();

        dto.setTotalBaskets(getLong(row[0]));
        dto.setTotalRevenue(new Money(getLong(row[1])));
        dto.setAverageBasketValue(new Money(getLong(row[2])));
        dto.setSmallestBasketValue(new Money(getLong(row[3])));
        dto.setLargestBasketValue(new Money(getLong(row[4])));
        dto.setAverageGrossBasketValue(new Money(getLong(row[5])));
        dto.setAverageDiscountPerBasket(new Money(getLong(row[6])));
        dto.setAverageTaxPerBasket(new Money(getLong(row[7])));
        dto.setAverageItemsPerBasket(getDouble(row[8]));
        dto.setAverageUniqueProductsPerBasket(getDouble(row[9]));
        dto.setAverageUnitsPerProduct(getDouble(row[10]));
        dto.setTotalItemsSold(getDouble(row[11]));
        dto.setDiscountedBasketCount(getLong(row[12]));
        dto.setDiscountedBasketPercentage(getDouble(row[13]));
        dto.setAverageDiscountRate(getDouble(row[14]));
        
        // Compute median only when supported
        if (isPostgres()) {
            dto.setMedianBasketValue(computeMedianBasketValue(params));
        }

        return dto;
    }
    
    private Long getLong(Object value) {

        if (value == null) {
            return 0L;
        }

        return ((Number) value).longValue();
    }

    private Double getDouble(Object value) {

        if (value == null) {
            return 0D;
        }

        return ((Number) value).doubleValue();
    }
    
    private boolean isPostgres() {
        return environment.getProperty(
        "spring.datasource.url", "")
        .contains("postgresql");
    }
    
    private Money computeMedianBasketValue(SalesReportQueryParams params) {

        StringBuilder sql = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();

        sql.append("""
            SELECT
                PERCENTILE_CONT(0.5)
                WITHIN GROUP (ORDER BY net_sales)
            FROM
            (
                SELECT
                    transaction_ref,
                    SUM(amount_in_micro_naira) AS net_sales
                FROM sale
                WHERE 1 = 1
            """);

        appendFilters(sql, parameters, params);

        sql.append("""
                GROUP BY transaction_ref
            ) basket
            """);

        Query query = em.createNativeQuery(sql.toString());

        parameters.forEach(query::setParameter);

        Object result = query.getSingleResult();

        if (result == null) {
            return new Money(0L);
        }

        return new Money(((Number) result).longValue());
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

        if (params.getProductCodes()!= null && !params.getProductCodes().isEmpty()) {
            sql.append(" AND product_code IN (:productCodes)");
            parameters.put("productCodes", params.getProductCodes());
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