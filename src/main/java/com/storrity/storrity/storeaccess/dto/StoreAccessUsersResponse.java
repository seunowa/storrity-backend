package com.storrity.storrity.storeaccess.dto;

import java.util.List;
import java.util.UUID;

public record StoreAccessUsersResponse(UUID storeId, List<StoreAccessUserResponse> users) {
}
