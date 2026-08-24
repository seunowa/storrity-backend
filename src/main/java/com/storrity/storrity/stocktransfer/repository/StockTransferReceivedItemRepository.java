package com.storrity.storrity.stocktransfer.repository;

import com.storrity.storrity.stocktransfer.entity.StockTransferReceivedItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransferReceivedItemRepository
        extends JpaRepository<StockTransferReceivedItem, UUID> {

    List<StockTransferReceivedItem> findByStockTransferId(UUID stockTransferId);
    void deleteByStockTransferId(UUID stockTransferId);
    boolean existsByStockTransferItemId(UUID stockTransferItemId);
}
