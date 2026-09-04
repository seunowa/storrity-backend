/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.inventory.report.repository;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.inventory.report.dto.*;
import com.storrity.storrity.product.entity.ProductType;
import com.storrity.storrity.product.entity.StockStatus;
import com.storrity.storrity.stocktransfer.entity.StockTransferStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Seun Owa
 *
 * Native-SQL implementation of the cross-cutting inventory reports.
 *
 * Current-state reports read from product because Product is the authoritative
 * current inventory snapshot. Historical/derived reports read from
 * stock_movement and transfer tables where appropriate.
 */
@Repository
public class InventoryReportRepositoryImpl implements InventoryReportRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public InventorySummaryDto inventorySummary(InventorySummaryQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*), COALESCE(SUM(p.qty_in_stock),0),
                   COALESCE(SUM(p.inventory_value_in_micro_naira),0)
            FROM product p
            WHERE 1=1
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), p.getProductIds(), p.getProductCodes(),
                p.getProductCategories(), p.getProductSubCategories(), p.getProductBrands(),
                p.getProductTypes(), p.getStockStatuses());
        Object[] r = one(sql, b);
        return InventorySummaryDto
                .builder()
                .productCount(getLong(r[0]))
                .totalQuantity(getDouble(r[1]))
                .totalInventoryValue(money(r[2]))
                .build();
    }

    @Override
    public List<InventoryByProductDto> inventoryByProduct(InventoryByProductQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,
                   p.category,p.subcategory,p.brand,p.product_type,p.stock_status,
                   p.qty_in_stock,p.unit_price_in_micro_naira,p.inventory_value_in_micro_naira
            FROM product p JOIN store s ON s.id=p.store_id WHERE 1=1
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), p.getProductIds(), p.getProductCodes(),
                p.getProductCategories(), p.getProductSubCategories(), p.getProductBrands(),
                p.getProductTypes(), p.getStockStatuses());
        sql.append(" ORDER BY p.name,p.code");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<InventoryByProductDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(InventoryByProductDto.builder()
                    .productId(uuid(r[0]))
                    .productName(str(r[1]))
                    .productCode(str(r[2]))
                    .baseUnit(str(r[3]))
                    .storeId(uuid(r[4]))
                    .storeName(str(r[5]))
                    .category(str(r[6]))
                    .subcategory(str(r[7]))
                    .brand(str(r[8]))
                    .productType(enumValue(ProductType.class, r[9]))
                    .stockStatus(enumValue(StockStatus.class, r[10]))
                    .quantityInStock(getDouble(r[11]))
                    .unitPrice(money(r[12]))
                    .inventoryValue(money(r[13]))
                    .build());
        }
        return out;
    }

    @Override
    public List<InventoryByStoreDto> inventoryByStore(InventoryByStoreQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.store_id,s.name,COUNT(*),COALESCE(SUM(p.qty_in_stock),0),
                   COALESCE(SUM(p.inventory_value_in_micro_naira),0)
            FROM product p JOIN store s ON s.id=p.store_id WHERE 1=1
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), null, null, p.getProductCategories(), null, p.getProductBrands(), p.getProductTypes(), p.getStockStatuses());
        sql.append(" GROUP BY p.store_id,s.name ORDER BY s.name");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<InventoryByStoreDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(InventoryByStoreDto
                    .builder()
                    .storeId(uuid(r[0]))
                    .storeName(str(r[1]))
                    .productCount(getLong(r[2]))
                    .totalQuantity(getDouble(r[3]))
                    .inventoryValue(money(r[4]))
                    .build());
        }
        return out;
    }

    @Override
    public List<InventoryByCategoryDto> inventoryByCategory(InventoryByCategoryQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.category,COUNT(*),COALESCE(SUM(p.qty_in_stock),0),
                   COALESCE(SUM(p.inventory_value_in_micro_naira),0)
            FROM product p WHERE 1=1
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), null, null, p.getProductCategories(), null, p.getProductBrands(), p.getProductTypes(), p.getStockStatuses());
        sql.append(" GROUP BY p.category ORDER BY p.category");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<InventoryByCategoryDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(InventoryByCategoryDto
                    .builder()
                    .category(str(r[0]))
                    .productCount(getLong(r[1]))
                    .totalQuantity(getDouble(r[2]))
                    .inventoryValue(money(r[3]))
                    .build());
        }
        return out;
    }

    @Override
    public List<InventoryByBrandDto> inventoryByBrand(InventoryByBrandQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.brand,COUNT(*),COALESCE(SUM(p.qty_in_stock),0),
                   COALESCE(SUM(p.inventory_value_in_micro_naira),0)
            FROM product p WHERE 1=1
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), null, null, p.getProductCategories(), null, p.getProductBrands(), p.getProductTypes(), p.getStockStatuses());
        sql.append(" GROUP BY p.brand ORDER BY p.brand");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<InventoryByBrandDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(InventoryByBrandDto
                    .builder()
                    .brand(str(r[0]))
                    .productCount(getLong(r[1]))
                    .totalQuantity(getDouble(r[2]))
                    .inventoryValue(money(r[3])).build());
        }
        return out;
    }

    @Override
    public List<InventoryByProductTypeDto> inventoryByProductType(InventoryByProductTypeQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.product_type,COUNT(*),COALESCE(SUM(p.qty_in_stock),0),
                   COALESCE(SUM(p.inventory_value_in_micro_naira),0)
            FROM product p WHERE 1=1
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), null, null, p.getProductCategories(), null, p.getProductBrands(), p.getProductTypes(), p.getStockStatuses());
        sql.append(" GROUP BY p.product_type ORDER BY p.product_type");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<InventoryByProductTypeDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(InventoryByProductTypeDto
                    .builder()
                    .productType(enumValue(ProductType.class, r[0]))
                    .productCount(getLong(r[1]))
                    .totalQuantity(getDouble(r[2]))
                    .inventoryValue(money(r[3]))
                    .build());
        }
        return out;
    }

    @Override
    public List<LowStockInventoryDto> lowStockInventory(LowStockInventoryQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,p.qty_in_stock,
                   p.minimum_stock_level,p.reorder_level,p.reorder_quantity,p.stock_status,p.inventory_value_in_micro_naira
            FROM product p JOIN store s ON s.id=p.store_id
            WHERE p.qty_in_stock < COALESCE(p.reorder_level,p.minimum_stock_level)
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), p.getProductSubCategories(), p.getProductBrands(), p.getProductTypes(), null);
        sql.append(" ORDER BY p.qty_in_stock ASC,p.name");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<LowStockInventoryDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(LowStockInventoryDto
                    .builder()
                    .productId(uuid(r[0]))
                    .productName(str(r[1]))
                    .productCode(str(r[2]))
                    .baseUnit(str(r[3]))
                    .storeId(uuid(r[4]))
                    .storeName(str(r[5]))
                    .quantityInStock(getDouble(r[6]))
                    .minimumStockLevel(dbl(r[7]))
                    .reorderLevel(dbl(r[8]))
                    .reorderQuantity(dbl(r[9]))
                    .stockStatus(enumValue(StockStatus.class, r[10]))
                    .inventoryValue(money(r[11]))
                    .build());
        }
        return out;
    }

    @Override
    public List<OutOfStockInventoryDto> outOfStockInventory(OutOfStockInventoryQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,p.qty_in_stock,
                   p.reorder_level,p.reorder_quantity,p.inventory_value_in_micro_naira
            FROM product p JOIN store s ON s.id=p.store_id WHERE p.qty_in_stock <= 0
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), p.getProductSubCategories(), p.getProductBrands(), p.getProductTypes(), null);
        sql.append(" ORDER BY p.name");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<OutOfStockInventoryDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(OutOfStockInventoryDto
                    .builder()
                    .productId(uuid(r[0]))
                    .productName(str(r[1]))
                    .productCode(str(r[2]))
                    .baseUnit(str(r[3]))
                    .storeId(uuid(r[4]))
                    .storeName(str(r[5]))
                    .quantityInStock(getDouble(r[6]))
                    .reorderLevel(dbl(r[7]))
                    .reorderQuantity(dbl(r[8]))
                    .inventoryValue(money(r[9]))
                    .build());
        }
        return out;
    }

    @Override
    public List<OverstockInventoryDto> overstockInventory(OverstockInventoryQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,p.qty_in_stock,
                   p.maximum_stock_level,(p.qty_in_stock-p.maximum_stock_level),p.inventory_value_in_micro_naira
            FROM product p JOIN store s ON s.id=p.store_id
            WHERE p.maximum_stock_level IS NOT NULL AND p.qty_in_stock > p.maximum_stock_level
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), p.getProductSubCategories(), p.getProductBrands(), p.getProductTypes(), null);
        sql.append(" ORDER BY (p.qty_in_stock-p.maximum_stock_level) DESC,p.name");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<OverstockInventoryDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(OverstockInventoryDto
                    .builder()
                    .productId(uuid(r[0]))
                    .productName(str(r[1]))
                    .productCode(str(r[2]))
                    .baseUnit(str(r[3]))
                    .storeId(uuid(r[4]))
                    .storeName(str(r[5]))
                    .quantityInStock(getDouble(r[6]))
                    .maximumStockLevel(dbl(r[7]))
                    .excessQuantity(getDouble(r[8]))
                    .inventoryValue(money(r[9]))
                    .build());
        }
        return out;
    }

    @Override
    public List<InventoryReorderRecommendationDto> inventoryReorderRecommendations(InvReorderRecQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,p.qty_in_stock,
                   p.reorder_level,p.reorder_quantity,p.maximum_stock_level,
                   CASE
                       WHEN p.maximum_stock_level IS NOT NULL AND p.maximum_stock_level > p.qty_in_stock
                           THEN CASE WHEN COALESCE(p.reorder_quantity,0) > (p.maximum_stock_level-p.qty_in_stock)
                                     THEN COALESCE(p.reorder_quantity,0) ELSE (p.maximum_stock_level-p.qty_in_stock) END
                       ELSE COALESCE(p.reorder_quantity,0)
                   END,
                   p.inventory_value_in_micro_naira
            FROM product p JOIN store s ON s.id=p.store_id
            WHERE p.reorder_level IS NOT NULL AND p.qty_in_stock < p.reorder_level
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), p.getProductSubCategories(), p.getProductBrands(), p.getProductTypes(), null);
        sql.append(" ORDER BY (p.reorder_level-p.qty_in_stock) DESC,p.name");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<InventoryReorderRecommendationDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(InventoryReorderRecommendationDto
                    .builder()
                    .productId(uuid(r[0]))
                    .productName(str(r[1]))
                    .productCode(str(r[2]))
                    .baseUnit(str(r[3]))
                    .storeId(uuid(r[4]))
                    .storeName(str(r[5]))
                    .quantityInStock(getDouble(r[6]))
                    .reorderLevel(dbl(r[7]))
                    .reorderQuantity(dbl(r[8]))
                    .maximumStockLevel(dbl(r[9]))
                    .suggestedReorderQuantity(dbl(r[10]))
                    .inventoryValue(money(r[11]))
                    .build());
        }
        return out;
    }

    @Override
    public List<ExpiringInventoryDto> expiringInventory(ExpiringInventoryQueryParams p) {
        return expiryReport(p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), p.getProductBrands(), p.getBatchNumbers(), p.getExpiryDateRange(), false, p.getOffset(), p.getLimit());
    }

    @Override
    public List<ExpiredInventoryDto> expiredInventory(ExpiredInventoryQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT sm.product_id,sm.product_name,sm.product_code,COALESCE(sm.sku,''),sm.store_id,sm.store_name,
                   sm.batch_number,sm.expiry_date,
                   COALESCE(SUM(sm.qty_in-sm.qty_out),0),
                   COALESCE(SUM(CASE WHEN sm.direction='INFLOW' THEN sm.movement_value_in_micro_naira ELSE -sm.movement_value_in_micro_naira END),0)
            FROM stock_movement sm WHERE sm.expiry_date IS NOT NULL AND sm.expiry_date < CURRENT_DATE
            """);
        Map<String, Object> b = new HashMap<>();
        appendMovementFilters(sql, b, "sm", p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), null, p.getProductBrands(), p.getBatchNumbers(), null);
        appendDateRange(sql, b, "sm.expiry_date", p.getExpiryDateRange());
        sql.append(" GROUP BY sm.product_id,sm.product_name,sm.product_code,sm.sku,sm.store_id,sm.store_name,sm.batch_number,sm.expiry_date ORDER BY sm.expiry_date");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<ExpiredInventoryDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            if (getDouble(r[9]) > 0) {
                out.add(ExpiredInventoryDto.builder().productId(uuid(r[0])).productName(str(r[1])).productCode(str(r[2])).baseUnit(str(r[3]))
                        .storeId(uuid(r[4])).storeName(str(r[5])).batchNumber(str(r[6])).expiryDate(date(r[7])).daysExpired(r[7] == null ? null : Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(date(r[7]), LocalDate.now()))).quantity(getDouble(r[8])).inventoryValue(money(r[9])).build());
            }
        }
        return out;
    }

    private List<ExpiringInventoryDto> expiryReport(List<UUID> stores, List<UUID> products, List<String> codes, List<String> cats, List<String> brands, List<String> batches, List<LocalDate> range, boolean expired, Integer offset, Integer limit) {
        StringBuilder sql = new StringBuilder("""
            SELECT sm.product_id,sm.product_name,sm.product_code,COALESCE(sm.sku,''),sm.store_id,sm.store_name,
                   sm.batch_number,sm.expiry_date,
                   COALESCE(SUM(sm.qty_in-sm.qty_out),0),
                   COALESCE(SUM(CASE WHEN sm.direction='INFLOW' THEN sm.movement_value_in_micro_naira ELSE -sm.movement_value_in_micro_naira END),0)
            FROM stock_movement sm WHERE sm.expiry_date IS NOT NULL AND sm.expiry_date >= CURRENT_DATE
            """);
        Map<String, Object> b = new HashMap<>();
        appendMovementFilters(sql, b, "sm", stores, products, codes, cats, null, brands, batches, null);
        appendDateRange(sql, b, "sm.expiry_date", range);
        sql.append(" GROUP BY sm.product_id,sm.product_name,sm.product_code,sm.sku,sm.store_id,sm.store_name,sm.batch_number,sm.expiry_date ORDER BY sm.expiry_date");
        Query q = query(sql, b);
        page(q, offset, limit);
        List<ExpiringInventoryDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            if (getDouble(r[9]) > 0) {
                out.add(ExpiringInventoryDto
                        .builder()
                        .productId(uuid(r[0]))
                        .productName(str(r[1]))
                        .productCode(str(r[2]))
                        .baseUnit(str(r[3]))
                        .storeId(uuid(r[4]))
                        .storeName(str(r[5]))
                        .batchNumber(str(r[6]))
                        .expiryDate(date(r[7]))
                        .daysUntilExpiry(r[7] == null ? null : Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), date(r[7]))))
                        .quantity(getDouble(r[8])).inventoryValue(money(r[9])).build());
            }
        }
        return out;
    }

    @Override
    public List<InventoryByBatchDto> inventoryByBatch(InventoryByBatchQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT sm.product_id,sm.product_name,sm.product_code,COALESCE(sm.sku,''),sm.store_id,sm.store_name,
                   sm.batch_number,MAX(sm.expiry_date),COALESCE(SUM(sm.qty_in-sm.qty_out),0),
                   COALESCE(SUM(CASE WHEN sm.direction='INFLOW' THEN sm.movement_value_in_micro_naira ELSE -sm.movement_value_in_micro_naira END),0)
            FROM stock_movement sm WHERE 1=1
            """);
        Map<String, Object> b = new HashMap<>();
        appendMovementFilters(sql, b, "sm", p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), null, p.getProductBrands(), p.getBatchNumbers(), null);
        appendDateRange(sql, b, "sm.expiry_date", p.getExpiryDateRange());
        sql.append(" GROUP BY sm.product_id,sm.product_name,sm.product_code,sm.sku,sm.store_id,sm.store_name,sm.batch_number ORDER BY sm.product_name,sm.batch_number");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<InventoryByBatchDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(InventoryByBatchDto
                    .builder()
                    .productId(uuid(r[0]))
                    .productName(str(r[1]))
                    .productCode(str(r[2]))
                    .baseUnit(str(r[3]))
                    .storeId(uuid(r[4]))
                    .storeName(str(r[5]))
                    .batchNumber(str(r[6]))
                    .expiryDate(date(r[7])).quantity(getDouble(r[8]))
                    .inventoryValue(money(r[9]))
                    .build());
        }
        return out;
    }

    @Override
    public List<SlowMovingInventoryDto> slowMovingInventory(SlowMovingInventoryQueryParams p) {
        return movementInactivityReport(p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), p.getProductBrands(), p.getCreatedAtRange(), p.getInactivityDays(), false, p.getOffset(), p.getLimit());
    }

    @Override
    public List<DeadStockInventoryDto> deadStockInventory(DeadStockInventoryQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,p.qty_in_stock,p.inventory_value_in_micro_naira,
                   p.last_movement_at,p.last_stock_out_at
            FROM product p JOIN store s ON s.id=p.store_id WHERE p.qty_in_stock > 0
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), null, p.getProductBrands(), null, null);
        if (p.getCreatedAtRange() != null && p.getCreatedAtRange().size() == 2) {
            sql.append(" AND p.created_at BETWEEN :from AND :to");
            b.put("from", p.getCreatedAtRange().get(0));
            b.put("to", p.getCreatedAtRange().get(1));
        }
        if (p.getInactivityDays() != null) {
            sql.append(" AND (p.last_stock_out_at IS NULL OR p.last_stock_out_at < :deadCutoff)");
        }
        if (p.getInactivityDays() != null) {
            b.put("deadCutoff", LocalDateTime.now().minusDays(p.getInactivityDays()));
        }
        sql.append(" ORDER BY p.last_stock_out_at NULLS FIRST,p.name");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<DeadStockInventoryDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(DeadStockInventoryDto
                    .builder().productId(uuid(r[0]))
                    .productName(str(r[1]))
                    .productCode(str(r[2]))
                    .baseUnit(str(r[3]))
                    .storeId(uuid(r[4]))
                    .storeName(str(r[5]))
                    .quantityInStock(getDouble(r[6]))
                    .inventoryValue(money(r[7]))
                    .lastMovementAt(dt(r[8]))
                    .lastStockOutAt(dt(r[9]))
                    .daysSinceLastStockOut(r[9] == null ? null : Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(dt(r[9]).toLocalDate(), LocalDate.now())))
                    .build());
        }
        return out;
    }

    private List<SlowMovingInventoryDto> movementInactivityReport(List<UUID> stores, List<UUID> products, List<String> codes, List<String> cats, List<String> brands, List<LocalDateTime> range, Integer inactivityDays, boolean dead, Integer offset, Integer limit) {
        // Slow-moving is defined here as positive current stock with no movement during the selected period.
        StringBuilder sql = new StringBuilder("""
            SELECT p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,p.qty_in_stock,p.inventory_value_in_micro_naira,
                   p.last_movement_at,p.last_stock_out_at
            FROM product p JOIN store s ON s.id=p.store_id WHERE p.qty_in_stock > 0
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", stores, products, codes, cats, null, brands, null, null);
        if (range != null && range.size() == 2) {
            sql.append(" AND p.last_movement_at BETWEEN :from AND :to");
            b.put("from", range.get(0));
            b.put("to", range.get(1));
        }
        if (inactivityDays != null) {
            sql.append(" AND (p.last_movement_at IS NULL OR p.last_movement_at < :cutoff)");
            b.put("cutoff", LocalDateTime.now().minusDays(inactivityDays));
        }
        sql.append(" ORDER BY p.last_movement_at NULLS FIRST,p.name");
        Query q = query(sql, b);
        page(q, offset, limit);
        List<SlowMovingInventoryDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(SlowMovingInventoryDto
                    .builder().productId(uuid(r[0]))
                    .productName(str(r[1]))
                    .productCode(str(r[2]))
                    .baseUnit(str(r[3]))
                    .storeId(uuid(r[4]))
                    .storeName(str(r[5]))
                    .quantityInStock(getDouble(r[6]))
                    .inventoryValue(money(r[7]))
                    .lastMovementAt(dt(r[8]))
                    .lastStockOutAt(dt(r[9]))
                    .daysSinceLastStockOut(r[9] == null ? null : Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(dt(r[9]).toLocalDate(), LocalDate.now())))
                    .build());
        }
        return out;
    }

    @Override
    public List<InventoryDaysOfSupplyDto> inventoryDaysOfSupply(InventoryDaysOfSupplyQueryParams p) {
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = p.getCreatedAtRange() != null && p.getCreatedAtRange().size() == 2 ? p.getCreatedAtRange().get(0) : to.minusDays(p.getLookbackDays() == null ? 30 : p.getLookbackDays());
        StringBuilder sql = new StringBuilder("""
            SELECT p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,p.qty_in_stock,
                   COALESCE(SUM(CASE WHEN sm.direction='OUTFLOW' THEN sm.qty_out ELSE 0 END),0),p.inventory_value_in_micro_naira
            FROM product p JOIN store s ON s.id=p.store_id
            LEFT JOIN stock_movement sm ON sm.product_id=p.id AND sm.store_id=p.store_id AND sm.created_at BETWEEN :from AND :to
            WHERE 1=1
            """);
        Map<String, Object> b = new HashMap<>();
        b.put("from", from);
        b.put("to", to);
        appendProductFilters(sql, b, "p", p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), null, p.getProductBrands(), null, null);
        sql.append(" GROUP BY p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,p.qty_in_stock,p.inventory_value_in_micro_naira ORDER BY p.name");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<InventoryDaysOfSupplyDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            double totalOutflow = getDouble(r[7]);
            double days = Math.max(1, java.time.Duration.between(from, to).toDays());
            double avg = totalOutflow / days;
            out.add(InventoryDaysOfSupplyDto
                    .builder()
                    .productId(uuid(r[0]))
                    .productName(str(r[1]))
                    .productCode(str(r[2]))
                    .baseUnit(str(r[3]))
                    .storeId(uuid(r[4]))
                    .storeName(str(r[5]))
                    .quantityInStock(getDouble(r[6]))
                    .averageDailyOutflow(avg)
                    .estimatedDaysOfSupply(avg > 0 ? getDouble(r[6]) / avg : null).inventoryValue(money(r[8]))
                    .build());
        }
        return out;
    }

    @Override
    public List<InventoryTurnoverDto> inventoryTurnover(InventoryTurnoverQueryParams p) {
        LocalDateTime to = p.getCreatedAtRange() != null && p.getCreatedAtRange().size() == 2 ? p.getCreatedAtRange().get(1) : LocalDateTime.now();
        LocalDateTime from = p.getCreatedAtRange() != null && p.getCreatedAtRange().size() == 2 ? p.getCreatedAtRange().get(0) : to.minusDays(30);
        StringBuilder sql = new StringBuilder("""
            SELECT p.store_id,s.name,
                   COALESCE(SUM(CASE WHEN sm.movement_type='SALE' AND sm.direction='OUTFLOW' THEN sm.movement_value_in_micro_naira ELSE 0 END),0),
                   COALESCE(AVG(p.inventory_value_in_micro_naira),0)
            FROM product p JOIN store s ON s.id=p.store_id
            LEFT JOIN stock_movement sm ON sm.product_id=p.id AND sm.store_id=p.store_id AND sm.created_at BETWEEN :from AND :to
            WHERE 1=1
            """);
        Map<String, Object> b = new HashMap<>();
        b.put("from", from);
        b.put("to", to);
        appendProductFilters(sql, b, "p", p.getStoreIds(), p.getProductIds(), p.getProductCodes(), p.getProductCategories(), null, p.getProductBrands(), null, null);
        sql.append(" GROUP BY p.store_id,s.name ORDER BY s.name");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<InventoryTurnoverDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            double cogsMicro = getDouble(r[2]);
            double avgMicro = getDouble(r[3]);
            out.add(InventoryTurnoverDto
                    .builder()
                    .storeId(uuid(r[0]))
                    .storeName(str(r[1]))
                    .periodStart(from.toLocalDate())
                    .periodEnd(to.toLocalDate())
                    .costOfGoodsSold(money(r[2]))
                    .averageInventoryValue(money(r[3]))
                    .turnoverRate(avgMicro > 0 ? cogsMicro / avgMicro : null)
                    .build());
        }
        return out;
    }

    @Override
    public List<InventoryBalanceReconciliationDto> inventoryBalanceReconciliation(InvBalanceRecQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,p.qty_in_stock,p.inventory_value_in_micro_naira,
                   COALESCE(SUM(sm.qty_in-sm.qty_out),0)
            FROM product p JOIN store s ON s.id=p.store_id LEFT JOIN stock_movement sm ON sm.product_id=p.id AND sm.store_id=p.store_id
            WHERE 1=1
            """);
        Map<String, Object> b = new HashMap<>();
        appendProductFilters(sql, b, "p", p.getStoreIds(), p.getProductIds(), p.getProductCodes(), null, null, null, null, null);
        if (p.getCreatedAtRange() != null && p.getCreatedAtRange().size() == 2) {
            sql.append(" AND sm.created_at BETWEEN :from AND :to");
            b.put("from", p.getCreatedAtRange().get(0));
            b.put("to", p.getCreatedAtRange().get(1));
        }
        sql.append(" GROUP BY p.id,p.name,p.code,p.stock_keeping_unit,p.store_id,s.name,p.qty_in_stock,p.inventory_value_in_micro_naira ORDER BY p.name");
        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());
        List<InventoryBalanceReconciliationDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            double current = getDouble(r[6]), calc = getDouble(r[8]);
            out.add(InventoryBalanceReconciliationDto
                    .builder()
                    .productId(uuid(r[0]))
                    .productName(str(r[1]))
                    .productCode(str(r[2]))
                    .baseUnit(str(r[3]))
                    .storeId(uuid(r[4]))
                    .storeName(str(r[5]))
                    .productStockQuantity(current)
                    .calculatedMovementBalance(calc)
                    .quantityVariance(current - calc)
                    .productInventoryValue(money(r[7]))
                    .build());
        }
        return out;
    }

    @Override
    public List<InventoryInTransitDto> inventoryInTransit(InventoryInTransitQueryParams p) {
        StringBuilder sql = new StringBuilder("""
            SELECT t.id,t.transaction_ref,t.source_store_id,t.source_store_name,
                   t.destination_store_id,t.destination_store_name,
                   i.product_id,i.product_name,i.product_code,i.base_unit,i.batch_number,
                   (i.quantity_sent-COALESCE(SUM(ri.quantity_received),0)),
                   ((i.quantity_sent-COALESCE(SUM(ri.quantity_received),0))*COALESCE(i.cost_price_in_micro_naira,0)),
                   t.transfer_status,t.sent_at
            FROM stock_transfer t
            JOIN stock_transfer_item i ON i.stock_transfer_id=t.id
            LEFT JOIN stock_transfer_received_item ri
              ON ri.stock_transfer_item_id=i.id
            WHERE t.transfer_status IN ('SENT','AWAITING_RECEIPT_APPROVAL','RECEIPT_APPROVED')
            """);

        Map<String, Object> b = new HashMap<>();
        inList(sql, b, "t.source_store_id", p.getSourceStoreIds(), "sourceStores");
        inList(sql, b, "t.destination_store_id", p.getDestinationStoreIds(), "destinationStores");
        inList(sql, b, "i.product_id", p.getProductIds(), "products");
        inList(sql, b, "i.product_code", p.getProductCodes(), "productCodes");
        inList(sql, b, "i.batch_number", p.getBatchNumbers(), "batches");
        enumList(sql, b, "t.transfer_status", p.getTransferStatuses(), "statuses");
        appendDateRange(sql, b, "t.sent_at", p.getSentAtRange());

        sql.append("""
            GROUP BY
                t.id,t.transaction_ref,t.source_store_id,t.source_store_name,
                t.destination_store_id,t.destination_store_name,
                i.id,i.product_id,i.product_name,i.product_code,i.base_unit,
                i.batch_number,i.quantity_sent,i.cost_price_in_micro_naira,
                t.transfer_status,t.sent_at
            HAVING i.quantity_sent-COALESCE(SUM(ri.quantity_received),0) > 0
            ORDER BY t.sent_at
            """);

        Query q = query(sql, b);
        page(q, p.getOffset(), p.getLimit());

        List<InventoryInTransitDto> out = new ArrayList<>();
        for (Object[] r : rows(q)) {
            out.add(InventoryInTransitDto.builder()
                    .transferId(uuid(r[0]))
                    .transactionRef(str(r[1]))
                    .sourceStoreId(uuid(r[2]))
                    .sourceStoreName(str(r[3]))
                    .destinationStoreId(uuid(r[4]))
                    .destinationStoreName(str(r[5]))
                    .productId(uuid(r[6]))
                    .productName(str(r[7]))
                    .productCode(str(r[8]))
                    .baseUnit(str(r[9]))
                    .batchNumber(str(r[10]))
                    .quantitySent(getDouble(r[11]))
                    .inventoryValue(money(r[12]))
                    .transferStatus(enumValue(StockTransferStatus.class, r[13]))
                    .sentAt(dt(r[14]))
                    .build());
        }
        return out;
    }

    private void appendProductFilters(StringBuilder sql, Map<String, Object> b, String a, List<UUID> stores, List<UUID> products, List<String> codes, List<String> cats, List<String> subcats, List<String> brands, List<ProductType> types, List<StockStatus> statuses) {
        inList(sql, b, a + ".store_id", stores, "storeIds");
        inList(sql, b, a + ".id", products, "productIds");
        inList(sql, b, a + ".code", codes, "productCodes");
        inList(sql, b, a + ".category", cats, "categories");
        inList(sql, b, a + ".subcategory", subcats, "subcategories");
        inList(sql, b, a + ".brand", brands, "brands");
        enumList(sql, b, a + ".product_type", types, "productTypes");
        enumList(sql, b, a + ".stock_status", statuses, "stockStatuses");
    }

    private void appendMovementFilters(StringBuilder sql, Map<String, Object> b, String a, List<UUID> stores, List<UUID> products, List<String> codes, List<String> cats, List<String> subcats, List<String> brands, List<String> batches, List<StockStatus> statuses) {
        inList(sql, b, a + ".store_id", stores, "storeIds");
        inList(sql, b, a + ".product_id", products, "productIds");
        inList(sql, b, a + ".product_code", codes, "productCodes");
        inList(sql, b, a + ".product_category", cats, "categories");
        inList(sql, b, a + ".product_sub_category", subcats, "subcategories");
        inList(sql, b, a + ".product_brand", brands, "brands");
        inList(sql, b, a + ".batch_number", batches, "batches");
        enumList(sql, b, a + ".stock_status", statuses, "stockStatuses");
    }

    private void appendDateRange(StringBuilder sql, Map<String, Object> b, String column, List<?> range) {
        if (range != null && range.size() == 2) {
            sql.append(" AND ").append(column).append(" BETWEEN :rangeFrom AND :rangeTo");
            b.put("rangeFrom", range.get(0));
            b.put("rangeTo", range.get(1));
        }
    }

    private void inList(StringBuilder sql, Map<String, Object> b, String column, Collection<?> v, String name) {
        if (v != null && !v.isEmpty()) {
            sql.append(" AND ").append(column).append(" IN (:").append(name).append(")");
            b.put(name, v);
        }
    }

    private void enumList(StringBuilder sql, Map<String, Object> b, String column, Collection<? extends Enum<?>> v, String name) {
        if (v != null && !v.isEmpty()) {
            sql.append(" AND ").append(column).append(" IN (:").append(name).append(")");
            b.put(name, v.stream().map(Enum::name).toList());
        }
    }

    private Query query(StringBuilder sql, Map<String, Object> b) {
        Query q = em.createNativeQuery(sql.toString());
        b.forEach(q::setParameter);
        return q;
    }

    private Object[] one(StringBuilder sql, Map<String, Object> b) {
        return (Object[]) query(sql, b).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> rows(Query q) {
        return q.getResultList();
    }

    private void page(Query q, Integer offset, Integer limit) {
        if (offset != null) {
            q.setFirstResult(offset);
        }
        if (limit != null) {
            q.setMaxResults(limit);
        }
    }

    private String str(Object v) {
        return v == null ? null : v.toString();
    }

//    private UUID uuid(Object v) {
//        return v == null ? null : (v instanceof UUID u ? u : UUID.fromString(v.toString()));
//    }
    private UUID uuid(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof UUID uuid) {
            return uuid;
        }

        if (value instanceof byte[] bytes) {
            if (bytes.length != 16) {
                throw new IllegalArgumentException(
                        "Expected 16 bytes for UUID but received "
                        + bytes.length
                );
            }

            ByteBuffer buffer = ByteBuffer.wrap(bytes);

            return new UUID(
                    buffer.getLong(),
                    buffer.getLong()
            );
        }

        if (value instanceof String string) {
            return UUID.fromString(string);
        }

        return UUID.fromString(value.toString());
    }

    private double getDouble(Object v) {
        return v == null ? 0D : ((Number) v).doubleValue();
    }

    private long getLong(Object v) {
        return v == null ? 0L : ((Number) v).longValue();
    }

    private Double dbl(Object v) {
        return v == null ? null : ((Number) v).doubleValue();
    }

    private LocalDate date(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof java.sql.Date d) {
            return d.toLocalDate();
        }
        if (v instanceof LocalDate d) {
            return d;
        }
        return LocalDate.parse(v.toString());
    }

    private LocalDateTime dt(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof java.sql.Timestamp t) {
            return t.toLocalDateTime();
        }
        if (v instanceof LocalDateTime d) {
            return d;
        }
        return LocalDateTime.parse(v.toString().replace(' ', 'T'));
    }

    private Money money(Object micro) {
        if (micro == null) {
            return null;
        }
        return Money.fromNaira(BigDecimal.valueOf(((Number) micro).longValue()).movePointLeft(4));
    }

    private <E extends Enum<E>> E enumValue(Class<E> type, Object v) {
        return v == null ? null : Enum.valueOf(type, v.toString());
    }
}
