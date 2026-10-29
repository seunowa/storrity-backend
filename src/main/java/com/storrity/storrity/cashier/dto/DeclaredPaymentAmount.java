package com.storrity.storrity.cashier.dto;
import com.storrity.storrity.cart.entity.PaymentMethod;
import com.storrity.storrity.cashaccounts.entity.Money;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
//@Todo implement PositiveOrZero constraint on Money here
public record DeclaredPaymentAmount(
        @NotNull PaymentMethod paymentMethod,
        UUID paymentChannelId,
        @NotNull String paymentChannel,
        @NotNull Money  declaredAmount,
        String notes) {}
