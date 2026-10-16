package com.storrity.storrity.storeaccess.dto;

import java.util.List;

public record AccessibleStoresResponse(
        String  username,
        boolean accessAllStores,
        List<AccessibleStoreResponse> stores) {
}
