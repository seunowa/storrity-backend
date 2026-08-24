package com.storrity.storrity.stocktransfer.dto;

import java.util.List;
import lombok.Data;

@Data
public class StockTransferReceiveDto {
    private List<StockTransferReceivedItemDto> items;
}
