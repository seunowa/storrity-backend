package com.storrity.storrity.stocktransfer.repository;

import com.storrity.storrity.stocktransfer.dto.StockTransferQueryParams;
import com.storrity.storrity.stocktransfer.entity.StockTransfer;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class StockTransferSpecifications {

    private StockTransferSpecifications() {}

    public static Specification<StockTransfer> from(StockTransferQueryParams p) {
        return Specification.where(equal("sourceStoreId", p.getSourceStoreId()))
            .and(equal("destinationStoreId", p.getDestinationStoreId()))
            .and(equal("transferStatus", p.getStatus()))
            .and(equal("transactionRef", p.getTransactionRef()))
            .and(dateRange(p))
            .and(productCode(p.getProductCode()));
    }

    private static <T> Specification<StockTransfer> equal(String field, T value) {
        return value == null ? null : (root, q, cb) -> cb.equal(root.get(field), value);
    }

    private static Specification<StockTransfer> dateRange(StockTransferQueryParams p) {
                
        List<LocalDate> range = p.getDateRange();
//        if (range == null || range.isEmpty()) return null;
//
//        LocalDate fromDate = range.get(0);
//        LocalDate toDate = range.size() > 1 ? range.get(1) : null;
//
//        if (fromDate == null && toDate == null) return null;
//
//        return (root, q, cb) -> {
//            LocalDateTime from = fromDate == null
//                ? LocalDateTime.MIN : fromDate.atStartOfDay();
//
//            LocalDateTime to = toDate == null
//                ? LocalDateTime.MAX : toDate.plusDays(1).atStartOfDay();
//
//            return cb.and(
//                cb.greaterThanOrEqualTo(root.get("createdAt"), from),
//                cb.lessThan(root.get("createdAt"), to)
//            );
//        };
//        
//        if (range == null || range.isEmpty()) return null;
//
//        LocalDate fromDate = range.get(0);
//        LocalDate toDate = range.size() > 1 ? range.get(1) : null;

        if (range == null || range.isEmpty()) return null;

        LocalDate fromDate = range.get(0);
        LocalDate toDate = range.size() > 1 ? range.get(1) : null;

        if (fromDate == null && toDate == null) return null;

        return (root, q, cb) -> {
            var createdAt = root.<java.time.LocalDateTime>get("createdAt");

            var from = fromDate == null
                ? null : cb.greaterThanOrEqualTo(createdAt, fromDate.atStartOfDay());
            var to = toDate == null
                ? null : cb.lessThan(createdAt, toDate.plusDays(1).atStartOfDay());

            if (from != null && to != null) return cb.and(from, to);
            return from != null ? from : to;
        };
    }

    private static Specification<StockTransfer> productCode(String code) {
        if (code == null || code.isBlank()) return null;

        return (root, q, cb) -> {
            /*
             * This works with the current scalar FK design only if the
             * persistence provider can join the child collection.
             * For large reporting queries, prefer a dedicated item query.
             */
            Join<Object, Object> items =
                root.join("itemsToSend", JoinType.LEFT);

            return cb.equal(items.get("productCode"), code);
        };
    }
}
