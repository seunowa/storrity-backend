package com.storrity.storrity.storeaccess.service;

import java.util.Set;
import java.util.UUID;

/**
 * Effective store-access scope resolved from the store-access policy and explicit assignments.
 * When accessAllStores is true, accessibleStoreIds is intentionally empty because the boolean
 * grants access to every store in the applicable client scope, including stores created later.
 */
public record StoreAccessContext(
        boolean accessAllStores,
        Set<UUID> accessibleStoreIds
) {
    public StoreAccessContext {
        accessibleStoreIds = accessibleStoreIds == null
                ? Set.of()
                : Set.copyOf(accessibleStoreIds);
        if (accessAllStores) {
            accessibleStoreIds = Set.of();
        }
    }
}
