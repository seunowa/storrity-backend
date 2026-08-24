package com.storrity.storrity.stocktransfer.repository;

import com.storrity.storrity.stocktransfer.entity.StockTransferProcessSettings;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransferProcessSettingsRepository
        extends JpaRepository<StockTransferProcessSettings, UUID> {

    Optional<StockTransferProcessSettings> findTopByOrderByCreatedAtAsc();
}
