package com.storrity.storrity.stocktransfer.repository;

import com.storrity.storrity.stocktransfer.dto.StockTransferQueryParams;
import com.storrity.storrity.stocktransfer.entity.StockTransfer;
import com.storrity.storrity.stocktransfer.entity.StockTransferStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.time.LocalDateTime;
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
        if (p.getFromDate() == null && p.getToDate() == null) return null;

        return (root, q, cb) -> {
            LocalDateTime from = p.getFromDate() == null
                ? LocalDateTime.MIN : p.getFromDate().atStartOfDay();

            LocalDateTime to = p.getToDate() == null
                ? LocalDateTime.MAX : p.getToDate().plusDays(1).atStartOfDay();

            return cb.and(
                cb.greaterThanOrEqualTo(root.get("createdAt"), from),
                cb.lessThan(root.get("createdAt"), to)
            );
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
