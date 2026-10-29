package com.storrity.storrity.cashier.dto;
import com.storrity.storrity.cart.entity.PaymentMethod;
import com.storrity.storrity.cashaccounts.entity.Money;
import java.time.LocalDateTime;
import java.util.UUID;
public record PaymentReconciliationView(
        UUID id,UUID cashierShiftId,
        UUID closingAttemptId,
        PaymentMethod paymentMethod,
        UUID paymentChannelId,
        String paymentChannel,
        Money expectedAmount,
        Money declaredAmount,
        Money variance,
        String notes,
        String submittedBy,
        LocalDateTime submittedAt
    ){}
