package com.storrity.storrity.cashier.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "cashier_cash_flow", uniqueConstraints = {
    @UniqueConstraint(name = "uk_cash_flow_source_type", columnNames = {"source_reference", "flow_type"})
}, indexes = {
    @Index(name = "idx_cash_flow_shift_created", columnList = "cashier_shift_id,created_at"),
    @Index(name = "idx_cash_flow_checkout", columnList = "checkout_id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashFlow {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(name = "cashier_shift_id", nullable = false, updatable = false)
    private UUID cashierShiftId;
    @Column(name = "store_id", nullable = false, updatable = false)
    private UUID storeId;
    @Column(name = "checkout_id", updatable = false)
    private UUID checkoutId;
    @Column(name = "checkout_payment_id", updatable = false)
    private UUID checkoutPaymentId;
    @Enumerated(EnumType.STRING)
    @Column(name = "flow_type", nullable = false, updatable = false, length = 40)
    private CashFlowType flowType; 
    @NotNull
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "inflow_in_micro_naira"))
    })
    private Money inflow;
    @NotNull
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "outflow_in_micro_naira"))
    })
    private Money outflow;
    @Column(name = "source_reference", nullable = false, updatable = false, length = 160)
    private String sourceReference;
    @Column(name = "transaction_ref", nullable = false, updatable = false, length = 160)
    private String transactionRef;
    @Column(name = "description", nullable = false, updatable = false, length = 500)
    private String description;
    @Column(name = "performed_by", nullable = false, updatable = false)
    private String performedBy;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        boolean in = inflow != null && inflow.getValueInMicroNaira() > 0;
        boolean out = outflow != null && outflow.getValueInMicroNaira() > 0;
        if (in == out) {
            throw new IllegalStateException("Exactly one positive inflow or outflow is required.");
        }
    }

    @Transient
    public long signedAmountInMicroNaira() {
        return inflow != null && inflow.getValueInMicroNaira() > 0 ? inflow.getValueInMicroNaira() : -outflow.getValueInMicroNaira();
    }
}
