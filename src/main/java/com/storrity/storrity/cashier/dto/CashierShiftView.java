package com.storrity.storrity.cashier.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.cashier.entity.CashierShiftStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CashierShiftView(
        UUID id,
        UUID storeId,
        String cashierUsername,
        CashierShiftStatus status,
        Money openingFloatInMicroNaira,
        LocalDateTime openedAt,
        String openedBy,
        LocalDateTime closingSubmittedAt, 
        String closingSubmittedBy,
        LocalDateTime closedAt,
        String closedBy,
        String closingNotes,
        List<PaymentReconciliationView> reconciliations
    ){}
