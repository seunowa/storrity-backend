package com.storrity.storrity.inventory.report.dto;

import jakarta.validation.constraints.Size;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query parameters for SlowMovingInventoryQueryParams.
 *
 * <p>The parameter object is intentionally specific to this report so that
 * callers are not required to supply unrelated inventory filters.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlowMovingInventoryQueryParams {

    private List<UUID> storeIds;
    private List<UUID> productIds;
    private List<String> productCodes;
    private List<String> productCategories;
    private List<String> productBrands;
    @Size(min = 2, max = 2, message = "createdAtRange must contain exactly two dates")
    private List<LocalDateTime> createdAtRange;
    private Integer inactivityDays;
    private Integer offset;
    private Integer limit;
}
