package com.storrity.storrity.storeaccess.service;

import com.storrity.storrity.storeaccess.dto.AccessibleStoresResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessCheckResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessPolicyRequest;
import com.storrity.storrity.storeaccess.dto.StoreAccessPolicyResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessUsersResponse;
import com.storrity.storrity.storeaccess.dto.StoreAssignmentRequest;
import java.util.UUID;

public interface StoreAccessService {
    AccessibleStoresResponse getAccessibleStores(String username);
    AccessibleStoresResponse replaceStoreAssignments(String username, StoreAssignmentRequest request, String actorUsername);
    StoreAccessPolicyResponse getPolicy(String username);
    StoreAccessPolicyResponse updatePolicy(String username, StoreAccessPolicyRequest request, String actorUsername);
    StoreAccessUsersResponse getUsersWithStoreAccess(UUID storeId);
    StoreAccessCheckResponse checkAccess(String username, UUID storeId);
    boolean hasStoreAccess(String username, UUID storeId);
    void revokeStoreAccess(String username, UUID storeId);
}
