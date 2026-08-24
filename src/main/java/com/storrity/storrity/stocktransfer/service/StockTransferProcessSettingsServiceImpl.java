package com.storrity.storrity.stocktransfer.service;

import com.storrity.storrity.stocktransfer.entity.StockTransferProcess;
import com.storrity.storrity.stocktransfer.entity.StockTransferProcessSettings;
import com.storrity.storrity.stocktransfer.entity.StockTransferProcessTemplate;
import com.storrity.storrity.stocktransfer.repository.StockTransferProcessSettingsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StockTransferProcessSettingsServiceImpl
        implements StockTransferProcessSettingsService {

    private final StockTransferProcessSettingsRepository repository;

    public StockTransferProcessSettingsServiceImpl(
            StockTransferProcessSettingsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public StockTransferProcess updateStockTransferProcessSettings(
            StockTransferProcess process) {

        if (process == null) {
            throw new IllegalArgumentException("Stock transfer process cannot be null");
        }

        process.validate();

        StockTransferProcessSettings settings =
            repository.findTopByOrderByCreatedAtAsc()
                .orElseGet(StockTransferProcessSettings::new);

        settings.setStockTransferProcess(process);
        repository.save(settings);

        return settings.getStockTransferProcess();
    }

    @Override
    public StockTransferProcess getStockTransferProcessSettings() {
        return repository.findTopByOrderByCreatedAtAsc()
            .map(StockTransferProcessSettings::getStockTransferProcess)
            .orElse(null);
    }

    @Override
    public StockTransferProcessTemplate getTemplates() {
        return new StockTransferProcessTemplate();
    }
}
