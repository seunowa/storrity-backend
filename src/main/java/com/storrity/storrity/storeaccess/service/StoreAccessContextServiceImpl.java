package com.storrity.storrity.storeaccess.service;

import com.storrity.storrity.security.service.AuthenticatedUser;
import com.storrity.storrity.storeaccess.query.StoreScopedQueryParams;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Applies the store scope embedded in the authenticated principal.
 */
@Service
public class StoreAccessContextServiceImpl implements StoreAccessContextService {

    @Override
    public AuthenticatedUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null 
                || !auth.isAuthenticated()
                || !(auth.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new AuthenticationCredentialsNotFoundException("An authenticated Storrity user is required");
        }
        return user;
    }

    @Override
    public Set<UUID> getAccessibleStoreIds() {
        return getCurrentUser().getAccessibleStoreIds();
    }

    @Override
    public boolean hasAccessToAllStores() {
        return getCurrentUser().hasAccessToAllStores();
    }

    @Override
    public boolean hasStoreAccess(UUID storeId) {
        return storeId != null && getCurrentUser().hasStoreAccess(storeId);
    }

    @Override
    public void requireStoreAccess(UUID storeId) {
        if (storeId == null) {
            throw new IllegalArgumentException("storeId is required");
        }
        if (!hasStoreAccess(storeId)) {
            throw new AccessDeniedException("You do not have access to the requested store");
        }
    }

    @Override
    public StoreAccessScope normalizeStoreIds(Collection<UUID> requestedStoreIds) {
        AuthenticatedUser user = getCurrentUser();
        Set<UUID> requested = requestedStoreIds == null ? Set.of() : requestedStoreIds.stream()
                .filter(Objects::nonNull).collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
        // Empty filter means every store available to this user.
        if (requested.isEmpty() && user.hasAccessToAllStores()) {
            return StoreAccessScope.allStores();
        }
        // Explicit filters are all-or-nothing; never silently strip unauthorized IDs.
        for (UUID storeId : requested) {
            if (!user.hasStoreAccess(storeId)) {
                throw new AccessDeniedException("The requested store filter contains a store you cannot access");
            }
        }
        if (requested.isEmpty()) {
            return StoreAccessScope.limitedTo(user.getAccessibleStoreIds());
        }
        return StoreAccessScope.limitedTo(requested);
    }

    @Override
    public StoreAccessScope normalizeStoreFilters(StoreScopedQueryParams params) {
        Objects.requireNonNull(params, "queryParams is required");
        StoreAccessScope scope = normalizeStoreIds(params.getStoreIds());
        if (scope.unrestricted()) {
            params.setStoreIds(null);
        } else {
            params.setStoreIds(List.copyOf(scope.storeIds()));
        }
        return scope;
    }
}
