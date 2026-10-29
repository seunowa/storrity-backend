package com.storrity.storrity.cashier.dto;
import com.storrity.storrity.cashaccounts.entity.Money;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
public record OpenCashierShiftRequest(@NotNull UUID storeId, Money openingFloat) {}
