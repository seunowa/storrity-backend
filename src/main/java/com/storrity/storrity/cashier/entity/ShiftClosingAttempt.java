package com.storrity.storrity.cashier.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "shift_closing_attempt", indexes = @Index(name = "idx_closing_attempt_shift_submitted", columnList = "cashier_shift_id,submitted_at"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftClosingAttempt {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(name = "cashier_shift_id", nullable = false, updatable = false)
    private UUID cashierShiftId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private ClosingAttemptStatus status;
    @Column(name = "submitted_by", nullable = false, updatable = false)
    private String submittedBy;
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;
    @Column(name = "closing_notes", length = 2000, updatable = false)
    private String closingNotes;
    @Column(name = "reviewed_by")
    private String reviewedBy;
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
    @Column(name = "review_reason", length = 2000)
    private String reviewReason;

    @PrePersist
    void prePersist() {
        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = ClosingAttemptStatus.SUBMITTED;
        }
    }
}
