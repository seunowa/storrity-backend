package com.storrity.storrity.inventory.report.dto;

import java.util.UUID;
import java.util.List;
import com.storrity.storrity.product.entity.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query parameters for InventoryReorderRecommendationQueryParams.
 *
 * <p>The parameter object is intentionally specific to this report so that
 * callers are not required to supply unrelated inventory filters.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvReorderRecQueryParams {

    private List<UUID> storeIds;
    private List<UUID> productIds;
    private List<String> productCodes;
    private List<String> productCategories;
    private List<String> productSubCategories;
    private List<String> productBrands;
    private List<ProductType> productTypes;
    private Integer offset;
    private Integer limit;
}
