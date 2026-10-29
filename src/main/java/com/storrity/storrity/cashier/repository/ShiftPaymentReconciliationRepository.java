package com.storrity.storrity.cashier.repository;
import com.storrity.storrity.cashier.entity.ShiftPaymentReconciliation;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ShiftPaymentReconciliationRepository extends JpaRepository<ShiftPaymentReconciliation,UUID> {
 List<ShiftPaymentReconciliation> findByCashierShiftIdAndClosingAttemptIdOrderByPaymentMethodAscPaymentChannelAsc(UUID shiftId,UUID attemptId);
 List<ShiftPaymentReconciliation> findByCashierShiftIdOrderBySubmittedAtDesc(UUID shiftId);
}
