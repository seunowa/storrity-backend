/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.service;

import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public class AuthenticatedUser {

    private final String username;
    private final String clientId;
    private final String clientName;
    private final boolean accessAllStores;
    private final Set<UUID> accessibleStoreIds;

    public AuthenticatedUser(String username, String clientId, String clientName,
            boolean accessAllStores, Set<UUID> accessibleStoreIds) {
        this.username = username;
        this.clientId = clientId;
        this.clientName = clientName;
        this.accessAllStores = accessAllStores;
        this.accessibleStoreIds = accessibleStoreIds == null
                ? Set.of()
                : Set.copyOf(accessibleStoreIds);
    }

    public String getUsername() {
        return username;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public boolean isAccessAllStores() {
        return accessAllStores;
    }

    public boolean hasAccessToAllStores() {
        return accessAllStores;
    }

    public Set<UUID> getAccessibleStoreIds() {
        return accessibleStoreIds;
    }

    public boolean hasStoreAccess(UUID storeId) {
        return storeId != null && (accessAllStores || accessibleStoreIds.contains(storeId));
    }   
}
