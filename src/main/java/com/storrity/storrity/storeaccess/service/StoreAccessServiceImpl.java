package com.storrity.storrity.storeaccess.service;

import com.storrity.storrity.security.entity.AppUser;
import com.storrity.storrity.security.repository.UserRepository;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.repository.StoreRepository;
import com.storrity.storrity.storeaccess.dto.AccessibleStoreResponse;
import com.storrity.storrity.storeaccess.dto.AccessibleStoresResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessCheckResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessPolicyRequest;
import com.storrity.storrity.storeaccess.dto.StoreAccessPolicyResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessUserResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessUsersResponse;
import com.storrity.storrity.storeaccess.dto.StoreAssignmentRequest;
import com.storrity.storrity.storeaccess.entity.UserStoreAccess;
import com.storrity.storrity.storeaccess.entity.UserStoreAccessPolicy;
import com.storrity.storrity.storeaccess.repository.UserStoreAccessPolicyRepository;
import com.storrity.storrity.storeaccess.repository.UserStoreAccessRepository;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StoreAccessServiceImpl implements StoreAccessService {
    private final UserStoreAccessRepository accessRepository;
    private final UserStoreAccessPolicyRepository policyRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public StoreAccessServiceImpl(UserStoreAccessRepository accessRepository,
            UserStoreAccessPolicyRepository policyRepository,
            StoreRepository storeRepository, UserRepository userRepository) {
        this.accessRepository = accessRepository;
        this.policyRepository = policyRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AccessibleStoresResponse getAccessibleStores(String username) {
        requireUser(username);
        boolean all = isAllStores(username);
        List<Store> stores;
        if (all) {
            stores = storeRepository.findAll();
        } else {
            List<UUID> ids = accessRepository.findAllByUsername(username).stream()
                    .map(UserStoreAccess::getStoreId).distinct().toList();
            stores = ids.isEmpty() ? List.of() : storeRepository.findAllById(ids).stream()
                    .sorted(Comparator.comparing(Store::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                    .toList();
        }
        if (all) stores = stores.stream()
                .sorted(Comparator.comparing(Store::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .toList();
        List<AccessibleStoreResponse> storeResponses = stores.stream().map(this::toStoreResponse).toList();
        return new AccessibleStoresResponse(username, all, storeResponses);
    }

    @Override
    @Transactional
    public AccessibleStoresResponse replaceStoreAssignments(String username, StoreAssignmentRequest request, String actorUserId) {
        requireUser(username);
        if (request == null || request.storeIds() == null) {
            throw new IllegalArgumentException("storeIds must be provided; use an empty list to remove all explicit assignments");
        }
        List<UUID> ids = request.storeIds().stream().distinct().toList();
        if (!ids.isEmpty()) {
            List<UUID> existing = storeRepository.findAllById(ids).stream().map(Store::getId).toList();
            Set<UUID> existingSet = new LinkedHashSet<>(existing);
            List<UUID> missing = ids.stream().filter(id -> !existingSet.contains(id)).toList();
            if (!missing.isEmpty()) throw new StoreAccessNotFoundException("Store(s) not found: " + missing);
        }
        // Delete and recreate inside one transaction, so a validation/database failure rolls back the replacement.
        accessRepository.deleteAllByUsername(username);
        accessRepository.flush();
        List<UserStoreAccess> assignments = ids.stream().map(storeId -> UserStoreAccess.builder()
                .username(username).storeId(storeId).createdBy(actorUserId).build()).toList();
        accessRepository.saveAll(assignments);
        accessRepository.flush();
        return getAccessibleStores(username);
    }

    @Override
    public StoreAccessPolicyResponse getPolicy(String username) {
        requireUser(username);
        return policyRepository.findByUsername(username)
                .map(this::toPolicyResponse)
                .orElseGet(() -> new StoreAccessPolicyResponse(username, false, null, null, null));
    }

    @Override
    @Transactional
    public StoreAccessPolicyResponse updatePolicy(String username, StoreAccessPolicyRequest request, String actorUsername) {
        requireUser(username);
        if (request == null) throw new IllegalArgumentException("Policy request is required");
        UserStoreAccessPolicy policy = policyRepository.findByUsername(username).orElseGet(() ->
                UserStoreAccessPolicy.builder().username(username).accessAllStores(false).build());
        policy.setAccessAllStores(request.accessAllStores());
        policy.setUpdatedBy(actorUsername);
        try {
            return toPolicyResponse(policyRepository.saveAndFlush(policy));
        } catch (DataIntegrityViolationException ex) {
            // The unique user_id constraint protects concurrent creation; caller may safely retry.
            throw ex;
        }
    }

    @Override
    public StoreAccessUsersResponse getUsersWithStoreAccess(UUID storeId) {
        requireStore(storeId);
        Set<String> usernames = new LinkedHashSet<>();
        accessRepository.findAllByStoreId(storeId).forEach(a -> usernames.add(a.getUsername()));
        policyRepository.findAllByAccessAllStoresTrue().forEach(p -> usernames.add(p.getUsername()));        
        List<StoreAccessUserResponse> result = usernames.stream()
                .map(u -> new StoreAccessUserResponse(u))
                .sorted(Comparator.comparing(StoreAccessUserResponse::username, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .toList();
        return new StoreAccessUsersResponse(storeId, result);
    }

    @Override
    public StoreAccessCheckResponse checkAccess(String username, UUID storeId) {
        requireUser(username);
        requireStore(storeId);
        return new StoreAccessCheckResponse(username, storeId, hasStoreAccess(username, storeId));
    }

    @Override
    public boolean hasStoreAccess(String username, UUID storeId) {
        if (username == null || storeId == null || !storeRepository.existsById(storeId)) return false;
        return isAllStores(username) || accessRepository.existsByUsernameAndStoreId(username, storeId);
    }

    @Override
    @Transactional
    public void revokeStoreAccess(String username, UUID storeId) {
        requireUser(username);
        requireStore(storeId);
        accessRepository.deleteByUsernameAndStoreId(username, storeId);
    }

    private boolean isAllStores(String username) {
        return policyRepository.findByUsername(username).map(UserStoreAccessPolicy::isAccessAllStores).orElse(false);
    }

    private AppUser requireUser(String username) {
        if (username == null) throw new IllegalArgumentException("username is required");
        return userRepository.findByUsername(username).orElseThrow(() -> new StoreAccessNotFoundException("User not found: " + username));
    }

    private Store requireStore(UUID storeId) {
        if (storeId == null) throw new IllegalArgumentException("storeId is required");
        return storeRepository.findById(storeId).orElseThrow(() -> new StoreAccessNotFoundException("Store not found: " + storeId));
    }

    private AccessibleStoreResponse toStoreResponse(Store store) {
        return new AccessibleStoreResponse(store.getId(), store.getName(), store.getPhone(), store.getEmail(),
                store.getState(), store.getCity(), store.getStreet(), store.getManagerName(),
                store.getManagerPhone(), store.getManagerEmail(), store.getManagerAddress(),
                store.getStatus(), store.getCreatedAt(), store.getUpdatedAt());
    }

    private StoreAccessPolicyResponse toPolicyResponse(UserStoreAccessPolicy p) {
        return new StoreAccessPolicyResponse(p.getUsername(), p.isAccessAllStores(), p.getCreatedAt(), p.getUpdatedAt(), p.getUpdatedBy());
    }
}
