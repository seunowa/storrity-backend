package com.storrity.storrity.storeaccess.controller;

import com.storrity.storrity.storeaccess.dto.AccessibleStoresResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessCheckResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessPolicyRequest;
import com.storrity.storrity.storeaccess.dto.StoreAccessPolicyResponse;
import com.storrity.storrity.storeaccess.dto.StoreAccessUsersResponse;
import com.storrity.storrity.storeaccess.dto.StoreAssignmentRequest;
import com.storrity.storrity.storeaccess.service.StoreAccessService;
import com.storrity.storrity.security.entity.AppUser;
import com.storrity.storrity.security.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/store-access")
@Tag(name = "Store Access", description = "Manage user-to-store access assignments and access scope")
@SecurityRequirement(name = "bearerAuth")
public class StoreAccessController {
    private final StoreAccessService storeAccessService;
    private final UserRepository userRepository;

    public StoreAccessController(StoreAccessService storeAccessService, UserRepository userRepository) {
        this.storeAccessService = storeAccessService;
        this.userRepository = userRepository;
    }

    @GetMapping("/users/{username}/stores")
    @PreAuthorize("hasAuthority('STORE_ACCESS_VIEW')")
    @Operation(summary = "Get stores a user can access")
    public AccessibleStoresResponse getAccessibleStores(@PathVariable String username) {
        return storeAccessService.getAccessibleStores(username);
    }

    @PutMapping("/users/{username}/stores")
    @PreAuthorize("hasAuthority('STORE_ACCESS_ASSIGN')")
    @Operation(summary = "Replace a user's explicit store assignments")
    public AccessibleStoresResponse replaceStoreAssignments(@PathVariable String username,
            @Valid @RequestBody StoreAssignmentRequest request, Authentication authentication) {
        return storeAccessService.replaceStoreAssignments(username, request, resolveActorId(authentication));
    }

    @GetMapping("/users/{username}/policy")
    @PreAuthorize("hasAuthority('STORE_ACCESS_VIEW')")
    @Operation(summary = "Get a user's store access policy")
    public StoreAccessPolicyResponse getPolicy(@PathVariable String username) {
        return storeAccessService.getPolicy(username);
    }

    @PutMapping("/users/{username}/policy")
    @PreAuthorize("hasAuthority('STORE_ACCESS_ALL_STORES')")
    @Operation(summary = "Set a user's selected-stores or all-stores access policy")
    public StoreAccessPolicyResponse updatePolicy(@PathVariable String username,
            @Valid @RequestBody StoreAccessPolicyRequest request, Authentication authentication) {
        return storeAccessService.updatePolicy(username, request, resolveActorId(authentication));
    }

    @GetMapping("/stores/{storeId}/users")
    @PreAuthorize("hasAuthority('STORE_ACCESS_VIEW_USERS')")
    @Operation(summary = "List users with effective access to a store, including username")
    public StoreAccessUsersResponse getUsersWithStoreAccess(@PathVariable UUID storeId) {
        return storeAccessService.getUsersWithStoreAccess(storeId);
    }

    @GetMapping("/users/{username}/stores/{storeId}")
    @PreAuthorize("hasAuthority('STORE_ACCESS_VIEW')")
    @Operation(summary = "Check whether a user can access a store")
    public StoreAccessCheckResponse checkAccess(@PathVariable String username, @PathVariable UUID storeId) {
        return storeAccessService.checkAccess(username, storeId);
    }

    @DeleteMapping("/users/{username}/stores/{storeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('STORE_ACCESS_REVOKE')")
    @Operation(summary = "Revoke one explicit store assignment")
    public void revokeStoreAccess(@PathVariable String username, @PathVariable UUID storeId) {
        storeAccessService.revokeStoreAccess(username, storeId);
    }

    private String resolveActorId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new AccessDeniedException("An authenticated application user is required for this operation");
        }
        return userRepository.findByUsername(authentication.getName())
                .map(AppUser::getUsername)
                .orElseThrow(() -> new AccessDeniedException("Authenticated principal is not an application user"));
    }
}
