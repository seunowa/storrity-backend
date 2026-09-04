/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.report.repository;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.supply.entity.SupplyStatus;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceByProductDto;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceBySupplierDto;
import com.storrity.storrity.supply.report.dto.DeliveryVarianceDto;
import com.storrity.storrity.supply.report.dto.ProductProcurementSummaryDto;
import com.storrity.storrity.supply.report.dto.SupplierLeadTimeDto;
import com.storrity.storrity.supply.report.dto.SupplierPerformanceDto;
import com.storrity.storrity.supply.report.dto.SupplyReportQueryParams;
import com.storrity.storrity.supply.report.dto.SupplyReportQueryParams.SupplyReportDateField;
import com.storrity.storrity.supply.report.dto.SupplyStatusSummaryDto;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Seun Owa
 *
 * SQL-backed implementation of the supply reporting repository.
 *
 * <p>The report repository deliberately reads the reporting/snapshot data from
 * supply, supply_order_item and supply_item. It does not depend on the normal
 * SupplyService lifecycle and therefore remains suitable for analytical reads.</p>
 */
@Repository
public class SupplyReportRepositoryImpl implements SupplyReportRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public SupplyReportRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SupplyStatusSummaryDto> supplyStatusSummary(SupplyReportQueryParams params) {
        StringBuilder sql = new StringBuilder("""
                SELECT s.supply_status,
                       COUNT(*) AS supply_count,
                       COALESCE(SUM(s.grand_total_in_micro_naira), 0) AS total_value
                FROM supply s
                WHERE 1 = 1
                """);

        MapSqlParameterSource values = new MapSqlParameterSource();
        appendSupplyFilters(sql, values, params, "s");
        sql.append(" GROUP BY s.supply_status ORDER BY s.supply_status");

        return jdbcTemplate.query(sql.toString(), values, (rs, rowNum) ->
                new SupplyStatusSummaryDto(
                        rs.getString("supply_status"),
                        rs.getLong("supply_count"),
                        rs.getLong("total_value")));
    }

    @Override
    public List<DeliveryVarianceDto> deliveryVariance(SupplyReportQueryParams params) {
        StringBuilder sql = new StringBuilder("""
                SELECT s.received_at,
                       s.supplier_id,
                       s.supplier_name,
                       oi.product_id,
                       oi.product_name,
                       oi.product_code,
                       s.store_id,
                       s.store_name,
                       oi.quantity_ordered,
                       COALESCE(si.quantity_received, 0) AS quantity_received,
                       oi.quantity_ordered - COALESCE(si.quantity_received, 0) AS quantity_variance
                FROM supply s
                JOIN supply_order_item oi ON oi.supply_id = s.id
                LEFT JOIN supply_item si ON si.order_item_id = oi.id
                WHERE 1 = 1
                """);

        MapSqlParameterSource values = new MapSqlParameterSource();
        appendSupplyFilters(sql, values, params, "s");
        sql.append(" ORDER BY s.received_at DESC, s.supplier_name, oi.product_name");
        appendPagination(sql, values, params);

        return jdbcTemplate.query(sql.toString(), values, (rs, rowNum) ->
                new DeliveryVarianceDto(
                        rs.getTimestamp("received_at") == null
                                ? null : rs.getTimestamp("received_at").toLocalDateTime(),
                        rs.getString("supplier_id"),
                        rs.getString("supplier_name"),
                        getString(rs, "product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_code"),
                        getString(rs, "store_id"),
                        rs.getString("store_name"),
                        getDouble(rs, "quantity_ordered"),
                        getDouble(rs, "quantity_received"),
                        getDouble(rs, "quantity_variance")));
    }

    @Override
    public List<SupplierPerformanceDto> supplierPerformance(SupplyReportQueryParams params) {
        StringBuilder sql = new StringBuilder("""
                SELECT s.id,
                       s.supplier_id,
                       s.supplier_name,
                       s.received_at,
                       s.expected_supply_date,
                       s.purchase_order_submitted_at,
                       s.grand_total_in_micro_naira
                FROM supply s
                WHERE 1 = 1
                """);

        MapSqlParameterSource values = new MapSqlParameterSource();
        appendSupplyFilters(sql, values, params, "s");
        sql.append(" ORDER BY s.supplier_name, s.received_at");

        List<PerformanceRow> rows = jdbcTemplate.query(sql.toString(), values, (rs, rowNum) ->
                new PerformanceRow(
                        getString(rs, "id"),
                        rs.getString("supplier_id"),
                        rs.getString("supplier_name"),
                        toLocalDateTime(rs.getTimestamp("received_at")),
                        toLocalDate(rs.getDate("expected_supply_date")),
                        toLocalDateTime(rs.getTimestamp("purchase_order_submitted_at")),
                        rs.getLong("grand_total_in_micro_naira")));

        Map<String, PerformanceAccumulator> grouped = new LinkedHashMap<>();
        for (PerformanceRow row : rows) {
            String key = row.supplierId() == null ? "" : row.supplierId();
            PerformanceAccumulator a = grouped.computeIfAbsent(key,
                    k -> new PerformanceAccumulator(row.supplierId(), row.supplierName()));

            a.totalOrders++;
            a.totalSpend += row.totalSpend();

            if (row.receivedAt() != null) {
                if (row.expectedSupplyDate() != null) {
                    long delayDays = ChronoUnit.DAYS.between(
                            row.expectedSupplyDate(), row.receivedAt().toLocalDate());
                    if (delayDays <= 0) {
                        a.onTimeOrders++;
                    } else {
                        a.lateOrders++;
                        a.delayDays.add((double) delayDays);
                    }
                }

                if (row.purchaseOrderSubmittedAt() != null) {
                    a.leadTimeHours.add((double) Duration.between(
                            row.purchaseOrderSubmittedAt(), row.receivedAt()).toMinutes() / 60.0);
                }
            }
        }

        return grouped.values().stream().map(a -> {
            Double averageDelay = a.delayDays.isEmpty()
                    ? 0D
                    : a.delayDays.stream().mapToDouble(Double::doubleValue).average().orElse(0D);

            SupplierPerformanceDto dto = new SupplierPerformanceDto(
                    a.supplierId,
                    a.supplierName,
                    a.totalOrders,
                    a.onTimeOrders,
                    a.lateOrders,
                    averageDelay,
                    a.totalSpend);

            dto.setAverageOrderValue(Money.ofMicroNaira(
                    a.totalOrders == 0 ? 0L : a.totalSpend / a.totalOrders));

            dto.setAvarageLeadTime(a.leadTimeHours.isEmpty()
                    ? 0L
                    : Math.round(a.leadTimeHours.stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0D)));

            return dto;
        }).toList();
    }

    @Override
    public List<DeliveryVarianceByProductDto> deliveryVarianceByProduct(SupplyReportQueryParams params) {
        StringBuilder sql = new StringBuilder("""
                SELECT oi.product_id,
                       oi.product_name,
                       oi.product_code,
                       COALESCE(SUM(oi.quantity_ordered), 0) AS quantity_ordered,
                       COALESCE(SUM(si.quantity_received), 0) AS quantity_received,
                       COALESCE(SUM(oi.quantity_ordered), 0)
                           - COALESCE(SUM(si.quantity_received), 0) AS quantity_variance
                FROM supply s
                JOIN supply_order_item oi ON oi.supply_id = s.id
                LEFT JOIN supply_item si ON si.order_item_id = oi.id
                WHERE 1 = 1
                """);

        MapSqlParameterSource values = new MapSqlParameterSource();
        appendSupplyFilters(sql, values, params, "s");
        sql.append(" GROUP BY oi.product_id, oi.product_name, oi.product_code");
        sql.append(" ORDER BY oi.product_name");
        appendPagination(sql, values, params);

        return jdbcTemplate.query(sql.toString(), values, (rs, rowNum) ->
                new DeliveryVarianceByProductDto(
                        getString(rs, "product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_code"),
                        getDouble(rs, "quantity_ordered"),
                        getDouble(rs, "quantity_received"),
                        getDouble(rs, "quantity_variance")));
    }

    @Override
    public List<DeliveryVarianceBySupplierDto> deliveryVarianceBySupplier(SupplyReportQueryParams params) {
        StringBuilder sql = new StringBuilder("""
                SELECT s.supplier_id,
                       s.supplier_name,
                       COALESCE(SUM(oi.quantity_ordered), 0) AS quantity_ordered,
                       COALESCE(SUM(si.quantity_received), 0) AS quantity_received,
                       COALESCE(SUM(oi.quantity_ordered), 0)
                           - COALESCE(SUM(si.quantity_received), 0) AS quantity_variance
                FROM supply s
                JOIN supply_order_item oi ON oi.supply_id = s.id
                LEFT JOIN supply_item si ON si.order_item_id = oi.id
                WHERE 1 = 1
                """);

        MapSqlParameterSource values = new MapSqlParameterSource();
        appendSupplyFilters(sql, values, params, "s");
        sql.append(" GROUP BY s.supplier_id, s.supplier_name");
        sql.append(" ORDER BY s.supplier_name");
        appendPagination(sql, values, params);

        return jdbcTemplate.query(sql.toString(), values, (rs, rowNum) ->
                new DeliveryVarianceBySupplierDto(
                        rs.getString("supplier_id"),
                        rs.getString("supplier_name"),
                        getDouble(rs, "quantity_ordered"),
                        getDouble(rs, "quantity_received"),
                        getDouble(rs, "quantity_variance")));
    }

    @Override
    public List<SupplierLeadTimeDto> supplierLeadTime(SupplyReportQueryParams params) {
        StringBuilder sql = new StringBuilder("""
                SELECT s.supplier_id,
                       s.supplier_name,
                       s.purchase_order_submitted_at,
                       s.received_at
                FROM supply s
                WHERE s.purchase_order_submitted_at IS NOT NULL
                  AND s.received_at IS NOT NULL
                """);

        MapSqlParameterSource values = new MapSqlParameterSource();
        appendSupplyFilters(sql, values, params, "s");

        List<LeadTimeRow> rows = jdbcTemplate.query(sql.toString(), values, (rs, rowNum) ->
                new LeadTimeRow(
                        rs.getString("supplier_id"),
                        rs.getString("supplier_name"),
                        toLocalDateTime(rs.getTimestamp("purchase_order_submitted_at")),
                        toLocalDateTime(rs.getTimestamp("received_at"))));

        Map<String, List<Double>> grouped = new LinkedHashMap<>();
        Map<String, String> names = new HashMap<>();
        for (LeadTimeRow row : rows) {
            String key = row.supplierId() == null ? "" : row.supplierId();
            names.putIfAbsent(key, row.supplierName());
            double days = Duration.between(row.submittedAt(), row.receivedAt()).toMinutes() / 1440.0;
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(days);
        }

        return grouped.entrySet().stream().map(entry -> {
            List<Double> valuesForSupplier = entry.getValue();
            valuesForSupplier.sort(Comparator.naturalOrder());
            return new SupplierLeadTimeDto(
                    entry.getKey().isEmpty() ? null : entry.getKey(),
                    names.get(entry.getKey()),
                    (long) valuesForSupplier.size(),
                    valuesForSupplier.get(0),
                    valuesForSupplier.stream().mapToDouble(Double::doubleValue).average().orElse(0D),
                    median(valuesForSupplier),
                    valuesForSupplier.get(valuesForSupplier.size() - 1));
        }).toList();
    }

    @Override
    public List<ProductProcurementSummaryDto> productProcurementSummary(SupplyReportQueryParams params) {
        StringBuilder sql = new StringBuilder("""
                SELECT s.id AS supply_id,
                       s.supplier_id,
                       s.supplier_name,
                       oi.product_id,
                       oi.quantity_ordered,
                       oi.cost_price_in_micro_naira,
                       COALESCE(si.quantity_received, 0) AS quantity_received
                FROM supply s
                JOIN supply_order_item oi ON oi.supply_id = s.id
                LEFT JOIN supply_item si ON si.order_item_id = oi.id
                WHERE 1 = 1
                """);

        MapSqlParameterSource values = new MapSqlParameterSource();
        appendSupplyFilters(sql, values, params, "s");
        sql.append(" ORDER BY oi.product_id, s.supplier_name");

        List<ProcurementRow> rows = jdbcTemplate.query(sql.toString(), values, (rs, rowNum) ->
                new ProcurementRow(
                        getString(rs, "supply_id"),
                        rs.getString("supplier_id"),
                        rs.getString("supplier_name"),
                        getString(rs, "product_id"),
                        getDouble(rs, "quantity_ordered"),
                        rs.getLong("cost_price_in_micro_naira"),
                        getDouble(rs, "quantity_received")));

        Map<String, ProcurementAccumulator> grouped = new LinkedHashMap<>();
        for (ProcurementRow row : rows) {
            ProcurementAccumulator a = grouped.computeIfAbsent(row.productId(),
                    k -> new ProcurementAccumulator());

            a.quantityOrdered += row.quantityOrdered();
            a.quantityReceived += row.quantityReceived();
            a.totalSpend += Math.round(row.quantityOrdered() * row.unitPrice());
            a.suppliers.add(row.supplierId());
            a.purchases.add(row.supplyId());

            if (row.unitPrice() < a.lowestPrice) {
                a.lowestPrice = row.unitPrice();
                a.lowestSupplier = row.supplierName();
            }
            if (row.unitPrice() > a.highestPrice) {
                a.highestPrice = row.unitPrice();
                a.highestSupplier = row.supplierName();
            }

            a.priceQuantity += row.quantityOrdered();
            a.weightedPriceTotal += row.unitPrice() * row.quantityOrdered();
        }

        return grouped.values().stream().map(a ->
                new ProductProcurementSummaryDto(
                        a.quantityOrdered,
                        a.quantityReceived,
                        a.quantityOrdered - a.quantityReceived,
                        a.totalSpend,
                        a.suppliers.size(),
                        a.purchases.size(),
                        a.priceQuantity == 0 ? 0L : Math.round(a.weightedPriceTotal / a.priceQuantity),
                        a.lowestPrice == Long.MAX_VALUE ? 0L : a.lowestPrice,
                        a.lowestSupplier,
                        a.highestPrice == Long.MIN_VALUE ? 0L : a.highestPrice,
                        a.highestSupplier))
                .toList();
    }

    private void appendSupplyFilters(
            StringBuilder sql,
            MapSqlParameterSource values,
            SupplyReportQueryParams params,
            String alias) {

        if (params == null) {
            return;
        }

        if (params.getStoreIds() != null && !params.getStoreIds().isEmpty()) {
            sql.append(" AND ").append(alias).append(".store_id IN (:storeIds)");
            values.addValue("storeIds", params.getStoreIds());
        }

        if (params.getSupplierIds() != null && !params.getSupplierIds().isEmpty()) {
            sql.append(" AND ").append(alias).append(".supplier_id IN (:supplierIds)");
            values.addValue("supplierIds", params.getSupplierIds());
        }

        if (params.getProductIds() != null && !params.getProductIds().isEmpty()) {
            sql.append(" AND EXISTS (SELECT 1 FROM supply_order_item f_oi "
                    + "WHERE f_oi.supply_id = " + alias + ".id "
                    + "AND f_oi.product_id IN (:productIds))");
            values.addValue("productIds", params.getProductIds());
        }

        if (params.getProductCategories() != null && !params.getProductCategories().isEmpty()) {
            sql.append(" AND EXISTS (SELECT 1 FROM supply_order_item f_oi "
                    + "WHERE f_oi.supply_id = " + alias + ".id "
                    + "AND f_oi.product_category IN (:productCategories))");
            values.addValue("productCategories", params.getProductCategories());
        }

        if (params.getSupplyStatuses() != null && !params.getSupplyStatuses().isEmpty()) {
            sql.append(" AND ").append(alias).append(".supply_status IN (:supplyStatuses)");
            values.addValue("supplyStatuses", params.getSupplyStatuses().stream()
                    .map(SupplyStatus::name)
                    .toList());
        }

        appendDateFilter(sql, values, params, alias);
    }

    private void appendDateFilter(
            StringBuilder sql,
            MapSqlParameterSource values,
            SupplyReportQueryParams params,
            String alias) {

        if (params.getDateRange() == null || params.getDateRange().isEmpty()) {
            return;
        }

        SupplyReportDateField dateField = params.getDateField() == null
                ? SupplyReportDateField.EXPECTED_SUPPLY
                : params.getDateField();

        String column = switch (dateField) {
            case EXPECTED_SUPPLY -> alias + ".expected_supply_date";
            case DRAFT_SUBMITTED -> alias + ".reporting_draft_submitted_date";
            case DRAFT_APPROVED -> alias + ".reporting_draft_approved_date";
            case DELIVERED -> alias + ".reporting_delivered_date";
            case DELIVERY_SUBMITTED -> alias + ".reporting_delivery_submitted_date";
            case DELIVERY_APPROVED -> alias + ".reporting_delivery_approved_date";
            case RECEIVED -> alias + ".reporting_received_date";
            case CANCELED -> alias + ".reporting_canceled_date";
        };

        sql.append(" AND ").append(column).append(" >= :dateFrom");
        sql.append(" AND ").append(column).append(" <= :dateTo");
        values.addValue("dateFrom", params.getDateRange().get(0));
        values.addValue("dateTo", params.getDateRange().get(1));
    }

    private void appendPagination(
            StringBuilder sql,
            MapSqlParameterSource values,
            SupplyReportQueryParams params) {

        if (params == null) {
            return;
        }

        if (params.getLimit() != null) {
            sql.append(" LIMIT :limit");
            values.addValue("limit", params.getLimit());
        }

        if (params.getOffset() != null) {
            sql.append(" OFFSET :offset");
            values.addValue("offset", params.getOffset());
        }
    }

    private static String getString(java.sql.ResultSet rs, String column) throws java.sql.SQLException {
        Object value = rs.getObject(column);
        return value == null ? null : value.toString();
    }

    private static Double getDouble(java.sql.ResultSet rs, String column) throws java.sql.SQLException {
        Object value = rs.getObject(column);
        return value == null ? null : ((Number) value).doubleValue();
    }

    private static LocalDateTime toLocalDateTime(java.sql.Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    private static LocalDate toLocalDate(java.sql.Date date) {
        return date == null ? null : date.toLocalDate();
    }

    private static double median(List<Double> values) {
        if (values.isEmpty()) {
            return 0D;
        }
        int middle = values.size() / 2;
        if (values.size() % 2 == 0) {
            return (values.get(middle - 1) + values.get(middle)) / 2D;
        }
        return values.get(middle);
    }

    private record PerformanceRow(
            String supplyId,
            String supplierId,
            String supplierName,
            LocalDateTime receivedAt,
            LocalDate expectedSupplyDate,
            LocalDateTime purchaseOrderSubmittedAt,
            long totalSpend) {
    }

    private static class PerformanceAccumulator {
        private final String supplierId;
        private final String supplierName;
        private long totalOrders;
        private long onTimeOrders;
        private long lateOrders;
        private long totalSpend;
        private final List<Double> delayDays = new ArrayList<>();
        private final List<Double> leadTimeHours = new ArrayList<>();

        private PerformanceAccumulator(String supplierId, String supplierName) {
            this.supplierId = supplierId;
            this.supplierName = supplierName;
        }
    }

    private record LeadTimeRow(
            String supplierId,
            String supplierName,
            LocalDateTime submittedAt,
            LocalDateTime receivedAt) {
    }

    private record ProcurementRow(
            String supplyId,
            String supplierId,
            String supplierName,
            String productId,
            double quantityOrdered,
            long unitPrice,
            double quantityReceived) {
    }

    private static class ProcurementAccumulator {
        private double quantityOrdered;
        private double quantityReceived;
        private long totalSpend;
        private final Set<String> suppliers = new HashSet<>();
        private final Set<String> purchases = new HashSet<>();
        private long lowestPrice = Long.MAX_VALUE;
        private String lowestSupplier;
        private long highestPrice = Long.MIN_VALUE;
        private String highestSupplier;
        private double priceQuantity;
        private double weightedPriceTotal;
    }
}