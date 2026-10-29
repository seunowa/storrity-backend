package com.storrity.storrity.cart.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

/**
 *
 * @author Seun Owa
 */
@Entity
@Table(
    name = "change",
    indexes = {
        @Index(name = "idx_change_checkout", columnList = "checkout_id"),
        @Index(name = "idx_change_cart", columnList = "cart_id"),
        @Index(name = "idx_change_shift", columnList = "cashier_shift_id"),
        @Index(name = "idx_change_payment_channel", columnList = "payment_channel_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Change {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "checkout_id", nullable = false, updatable = false)
    private UUID checkoutId;

    @Column(name = "cart_id", nullable = false, updatable = false)
    private UUID cartId;
    
    private UUID storeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, updatable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_channel_id", nullable = false, updatable = false)
    private UUID paymentChannelId;

    @Column(name = "payment_channel", nullable = false, updatable = false)
    private String paymentChannel;

    @AttributeOverrides({
        @AttributeOverride(
            name = "valueInMicroNaira",
            column = @Column(
                name = "amount_in_micro_naira",
                nullable = false,
                updatable = false
            )
        )
    })
    private Money amount;

    @Column(name = "transaction_ref", nullable = false, updatable = false)
    private String transactionRef;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "performed_by", nullable = false, updatable = false)
    private String performedBy;

    @Column(name = "cashier_shift_id", updatable = false)
    private UUID cashierShiftId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }
    }
}