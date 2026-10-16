package com.storrity.storrity.storeaccess.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User store-access scope")
public record StoreAccessPolicyRequest(
        @Schema(description = "True grants access to all stores, including stores created later")
        boolean accessAllStores) {
}
