package com.storrity.storrity.storeaccess.query;

import java.util.List;
import java.util.UUID;

/**
 * Implement this interface on query-parameter DTOs that expose a storeIds filter.
 *Query DTO contract: null means unrestricted after authorization; empty means no stores.
 */
public interface StoreScopedQueryParams {
    List<UUID> getStoreIds();
    void setStoreIds(List<UUID> storeIds);
}
