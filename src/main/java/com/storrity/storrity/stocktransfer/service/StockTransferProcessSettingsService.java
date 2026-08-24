package com.storrity.storrity.stocktransfer.service;

import com.storrity.storrity.stocktransfer.entity.StockTransferProcess;
import com.storrity.storrity.stocktransfer.entity.StockTransferProcessTemplate;

public interface StockTransferProcessSettingsService {
    StockTransferProcess updateStockTransferProcessSettings(StockTransferProcess process);
    StockTransferProcess getStockTransferProcessSettings();
    StockTransferProcessTemplate getTemplates();
}
