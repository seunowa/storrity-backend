package com.storrity.storrity.storeaccess.dto;

import java.util.UUID;

public record StoreAccessCheckResponse(String username, UUID storeId, boolean hasAccess) {
}
