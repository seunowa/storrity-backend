package com.storrity.storrity.storeaccess.dto;

import java.time.LocalDateTime;

public record StoreAccessPolicyResponse(
        String usename,
        boolean accessAllStores,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String updatedBy) {
}
