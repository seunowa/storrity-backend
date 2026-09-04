package com.storrity.storrity.inventory.report.dto;

import jakarta.validation.constraints.Size;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;
import com.storrity.storrity.stocktransfer.entity.StockTransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query parameters for InventoryInTransitQueryParams.
 *
 * <p>The parameter object is intentionally specific to this report so that
 * callers are not required to supply unrelated inventory filters.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryInTransitQueryParams {

    private List<UUID> sourceStoreIds;
    private List<UUID> destinationStoreIds;
    private List<UUID> productIds;
    private List<String> productCodes;
    private List<String> batchNumbers;
    private List<StockTransferStatus> transferStatuses;
    @Size(min = 2, max = 2, message = "sentAtRange must contain exactly two dates")
    private List<LocalDateTime> sentAtRange;
    private Integer offset;
    private Integer limit;
}
