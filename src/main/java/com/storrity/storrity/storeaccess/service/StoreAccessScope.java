package com.storrity.storrity.storeaccess.service;

import java.util.Set;
import java.util.UUID;

/**
 * Effective store boundary for one business query.
 */
public record StoreAccessScope(boolean unrestricted, Set<UUID> storeIds) {

    public StoreAccessScope  {
        storeIds = storeIds == null ? Set.of() : Set.copyOf(storeIds);
        if (unrestricted && !storeIds.isEmpty()) {
            throw new IllegalArgumentException("An unrestricted scope must not contain store IDs");
        }
    }

    public static StoreAccessScope allStores() {
        return new StoreAccessScope(true, Set.of());
    }

    public static StoreAccessScope limitedTo(Set<UUID> storeIds) {
        return new StoreAccessScope(false, storeIds);
    }

    public boolean hasNoStores() {
        return !unrestricted && storeIds.isEmpty();
    }

    public boolean allows(UUID storeId) {
        return storeId != null && (unrestricted || storeIds.contains(storeId));
    }
}
