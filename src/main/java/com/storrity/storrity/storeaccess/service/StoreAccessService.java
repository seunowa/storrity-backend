package com.storrity.storrity.storeaccess.service;

import com.storrity.storrity.storeaccess.dto.AccessibleStoresResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessCheckResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessPolicyRequest;
import com.storrity.storrity.storeaccess.dto.StoreAccessPolicyResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessUsersResponse;
import com.storrity.storrity.storeaccess.dto.StoreAssignmentRequest;
import java.util.UUID;

public interface StoreAccessService {
    public StoreAccessContext getStoreAccessContext(String username);
    public AccessibleStoresResponse getAccessibleStores(String username);
    public AccessibleStoresResponse replaceStoreAssignments(String username, StoreAssignmentRequest request, String actorUsername);
    public StoreAccessPolicyResponse getPolicy(String username);
    public StoreAccessPolicyResponse updatePolicy(String username, StoreAccessPolicyRequest request, String actorUsername);
    public StoreAccessUsersResponse getUsersWithStoreAccess(UUID storeId);
    public StoreAccessCheckResponse checkAccess(String username, UUID storeId);
    public boolean hasStoreAccess(String username, UUID storeId);
    public void revokeStoreAccess(String username, UUID storeId);
}
