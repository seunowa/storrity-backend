package com.storrity.storrity.stocktransfer.service;

import com.storrity.storrity.stocktransfer.dto.StockTransferCreationDto;
import com.storrity.storrity.stocktransfer.dto.StockTransferDto;
import com.storrity.storrity.stocktransfer.dto.StockTransferQueryParams;
import com.storrity.storrity.stocktransfer.dto.StockTransferReceiveDto;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

public interface StockTransferService {
    StockTransferDto createDraft(StockTransferCreationDto dto);
    StockTransferDto fetch(UUID id);
    List<StockTransferDto> list(StockTransferQueryParams params);
    CountDto count(StockTransferQueryParams params);
    StockTransferDto updateDraft(UUID id, StockTransferCreationDto dto);
    StockTransferDto submitDraft(UUID id);
    StockTransferDto approveDraft(UUID id);
    StockTransferDto send(UUID id);
    StockTransferDto submitReceipt(UUID id);
    StockTransferDto approveReceipt(UUID id);
    StockTransferDto receive(UUID id, StockTransferReceiveDto dto);
    StockTransferDto cancel(UUID id);
    StockTransferDto delete(UUID id);
}
