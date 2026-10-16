package com.storrity.storrity.storeaccess.dto;

import com.storrity.storrity.store.entity.StoreStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/** Explicit API projection; avoids serializing the JPA entity directly. */
public record AccessibleStoreResponse(
        UUID id,
        String name,
        String phone,
        String email,
        String state,
        String city,
        String street,
        String managerName,
        String managerPhone,
        String managerEmail,
        String managerAddress,
        StoreStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
