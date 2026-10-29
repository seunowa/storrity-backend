package com.storrity.storrity.cashier.service;

import com.storrity.storrity.cashier.entity.*;
import com.storrity.storrity.cashier.repository.CashFlowRepository;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.cart.entity.CheckoutPayment;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CasierCashFlowService {

    private final CashFlowRepository repo;

    public CasierCashFlowService(CashFlowRepository repo) {
        this.repo = repo;
    }

    /**
     * Records checkout-level physical cash movements. Tender and change are independent:
     * a checkout may have cash tender only, cash change only, both, or neither.
     * The checkout transactionRef, rather than an individual payment, identifies the source.
     */
    @Transactional
    public void recordCheckoutCashMovements(CashierShift shift, UUID checkoutId,
            String transactionRef, Money tendered, Money changeGiven, String username) {
        if (shift == null || checkoutId == null || transactionRef == null || transactionRef.isBlank()) {
            throw new IllegalArgumentException("Shift, checkout ID and transaction reference are required.");
        }
        long tender = value(tendered);
        long change = value(changeGiven);
        if (tender < 0 || change < 0) {
            throw new IllegalArgumentException("Cash tender and cash change cannot be negative.");
        }
        String source = "CHECKOUT:" + transactionRef;
        Money zero = Money.ofMicroNaira(0L);

        if (tender > 0) {
            if (repo.existsBySourceReferenceAndFlowType(source, CashFlowType.CHECKOUT_CASH_TENDER)) {
                throw new IllegalStateException("Cash tender flow already exists for checkout " + transactionRef);
            }
            repo.save(CashFlow.builder()
                    .cashierShiftId(shift.getId())
                    .storeId(shift.getStoreId())
                    .checkoutId(checkoutId)
                    .checkoutPaymentId(null)
                    .flowType(CashFlowType.CHECKOUT_CASH_TENDER)
                    .inflow(Money.ofMicroNaira(tender))
                    .outflow(zero)
                    .sourceReference(source)
                    .transactionRef(transactionRef)
                    .description("Cash tender received for checkout " + transactionRef)
                    .performedBy(username)
                    .build());
        }

        if (change > 0) {
            if (repo.existsBySourceReferenceAndFlowType(source, CashFlowType.CHECKOUT_CHANGE_GIVEN)) {
                throw new IllegalStateException("Cash change flow already exists for checkout " + transactionRef);
            }
            repo.save(CashFlow.builder()
                    .cashierShiftId(shift.getId())
                    .storeId(shift.getStoreId())
                    .checkoutId(checkoutId)
                    .checkoutPaymentId(null)
                    .flowType(CashFlowType.CHECKOUT_CHANGE_GIVEN)
                    .inflow(zero)
                    .outflow(Money.ofMicroNaira(change))
                    .sourceReference(source)
                    .transactionRef(transactionRef)
                    .description("Cash change given for checkout " + transactionRef)
                    .performedBy(username)
                    .build());
        }
    }

    /**
     * Called by the separate refund workflow when a refund is physically paid by cashier from drawer
     * in cash.
     * @param shift
     * @param refundId
     * @param transactionRef
     * @param amount
     * @param username
     * @return 
     */
//    @Todo consider refactoring out this code as it is not needed in current implementaion of sales return
    @Deprecated
    @Transactional
    public CashFlow recordRefundCashOut(CashierShift shift, UUID refundId, String transactionRef, Money amount, String username) {
        if (!amount.isPositiveValue()) {
            throw new IllegalArgumentException("Cash refund amount must be positive.");
        }
        String source = "REFUND:" + transactionRef;
        if (repo.existsBySourceReferenceAndFlowType(source, CashFlowType.REFUND_CASH_OUT)) {
            throw new IllegalStateException("Cash outflow already exists for this refund.");
        }
        return repo.save(CashFlow
                .builder()
                .cashierShiftId(shift.getId())
                .storeId(shift.getStoreId())
                .flowType(CashFlowType.REFUND_CASH_OUT)
                .outflow(amount).sourceReference(source)
                .description("Cash refund " + refundId)
                .performedBy(username)
                .build());
    }

    private long value(Money money) {
        return money == null ? 0L : money.getValueInMicroNaira();
    }

}
