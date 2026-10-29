package com.storrity.storrity.cashier.dto;

import com.storrity.storrity.cart.entity.PaymentMethod;
import com.storrity.storrity.cashaccounts.entity.Money;
import java.util.UUID;

public record ExpectedPaymentAmount(
        PaymentMethod paymentMethod,
        UUID paymentChannelId,
        String paymentChannel,
        Money expectedAmount
) {}
