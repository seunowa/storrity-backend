package com.storrity.storrity.stocktransfer.dto;

import com.storrity.storrity.stocktransfer.entity.StockTransferStatus;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class StockTransferQueryParams {
    private UUID sourceStoreId;
    private UUID destinationStoreId;
    private UUID storeId;
    private StockTransferStatus status;
    private String productCode;
    private String transactionRef;
//    private LocalDate fromDate;
//    private LocalDate toDate;
    @Size(min = 2, max = 2, message = "dateRange must contain exactly two dates")
    private List<LocalDate> dateRange;
    private Integer offset;
    private Integer limit;
}
