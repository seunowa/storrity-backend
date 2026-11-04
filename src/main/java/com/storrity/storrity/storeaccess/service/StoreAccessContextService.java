package com.storrity.storrity.storeaccess.service;

import com.storrity.storrity.security.service.AuthenticatedUser;
import com.storrity.storrity.storeaccess.query.StoreScopedQueryParams;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/** Runtime store authorization based on the verified JWT principal; no DB lookups. */
public interface StoreAccessContextService {
    public AuthenticatedUser getCurrentUser();
    public Set<UUID> getAccessibleStoreIds();
    public boolean hasAccessToAllStores();
    public boolean hasStoreAccess(UUID storeId);
    public void requireStoreAccess(UUID storeId);
    public StoreAccessScope normalizeStoreIds(Collection<UUID> requestedStoreIds);
    public StoreAccessScope normalizeStoreFilters(StoreScopedQueryParams queryParams);
}
