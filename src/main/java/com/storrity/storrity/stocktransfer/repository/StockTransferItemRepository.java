package com.storrity.storrity.stocktransfer.repository;

import com.storrity.storrity.stocktransfer.entity.StockTransferItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransferItemRepository
        extends JpaRepository<StockTransferItem, UUID> {

    List<StockTransferItem> findByStockTransferId(UUID stockTransferId);
    void deleteByStockTransferId(UUID stockTransferId);
}
