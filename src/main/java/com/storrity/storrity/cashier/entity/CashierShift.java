package com.storrity.storrity.cashier.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "cashier_shift", indexes = {
    @Index(name = "idx_cashier_shift_store_status", columnList = "store_id,status"),
    @Index(name = "idx_cashier_shift_cashier_status", columnList = "cashier_username,status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashierShift {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(name = "store_id", nullable = false, updatable = false)
    private UUID storeId;
    @Column(name = "cashier_username", nullable = false, updatable = false)
    private String cashierUsername;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private CashierShiftStatus status;   
    @NotNull
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "opening_float_in_micro_naira"))
    })
    private Money openingFloat;
    @Column(name = "opened_at", nullable = false, updatable = false)
    private LocalDateTime openedAt;
    @Column(name = "opened_by", nullable = false, updatable = false)
    private String openedBy;
    @Column(name = "closing_submitted_at")
    private LocalDateTime closingSubmittedAt;
    @Column(name = "closing_submitted_by")
    private String closingSubmittedBy;
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    @Column(name = "closed_by")
    private String closedBy;
    @Column(name = "closing_notes", length = 2000)
    private String closingNotes;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Version
    private long version;

    @PrePersist
    void prePersist() {
        LocalDateTime n = LocalDateTime.now();
        if (status == null) {
            status = CashierShiftStatus.OPEN;
        }
        if (openedAt == null) {
            openedAt = n;
        }
        createdAt = n;
        updatedAt = n;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
