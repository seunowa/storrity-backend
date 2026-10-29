package com.storrity.storrity.cashier.entity;

import com.storrity.storrity.cart.entity.PaymentMethod;
import com.storrity.storrity.cashaccounts.entity.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "shift_payment_reconciliation", uniqueConstraints = @UniqueConstraint(
        name = "uk_shift_recon_attempt_method_channel", columnNames = {"closing_attempt_id", "payment_method", "payment_channel_id"}),
        indexes = @Index(name = "idx_shift_recon_shift_attempt", columnList = "cashier_shift_id,closing_attempt_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftPaymentReconciliation {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(name = "cashier_shift_id", nullable = false, updatable = false)
    private UUID cashierShiftId;
    @Column(name = "closing_attempt_id", nullable = false, updatable = false)
    private UUID closingAttemptId;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, updatable = false, length = 40)
    private PaymentMethod paymentMethod;
    @Column(name = "payment_channel_id", updatable = false)
    private UUID paymentChannelId;
    @Column(name = "payment_channel", nullable = false, updatable = false, length = 120)
    private String paymentChannel;   
    @NotNull
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "expected_amount_in_micro_naira"))
    })
    private Money expectedAmount;   
    @NotNull
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "declared_amount_in_micro_naira"))
    })
    private Money declaredAmount;  
    @NotNull
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "variance_in_micro_naira"))
    })
    private Money variance;
    @Column(name = "notes", length = 2000, updatable = false)
    private String notes;
    @Column(name = "submitted_by", nullable = false, updatable = false)
    private String submittedBy;
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    void prePersist() {
        submittedAt = LocalDateTime.now();
        variance = declaredAmount.subtract(expectedAmount);
    }
}
