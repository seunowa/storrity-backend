package com.storrity.storrity.cashier.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.cashier.entity.CashFlowType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

//@Todo constrain money value here to a positiveOrZeroValue
public record CashFlowRequest(@NotNull
        CashFlowType type, Money amount, @NotBlank
        String description, String reference) {

}
