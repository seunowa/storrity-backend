package com.storrity.storrity.storeaccess.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Schema(description = "The complete set of explicit store assignments for a user")
public record StoreAssignmentRequest(
        @NotNull @Schema(description = "Store UUIDs to assign; an empty list removes all explicit assignments")
        List<@NotNull UUID> storeIds) {
}
