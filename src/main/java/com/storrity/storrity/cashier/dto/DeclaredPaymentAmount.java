package com.storrity.storrity.cashier.dto;
import com.storrity.storrity.cashaccounts.entity.Money;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
//@Todo implement PositiveOrZero constraint on Money here
public record DeclaredPaymentAmount(
        UUID paymentChannelId,
        @NotNull Money  declaredAmount,
        String notes
) {}
