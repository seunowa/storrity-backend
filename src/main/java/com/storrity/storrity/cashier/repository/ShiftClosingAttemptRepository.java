package com.storrity.storrity.cashier.repository;
import com.storrity.storrity.cashier.entity.ShiftClosingAttempt;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ShiftClosingAttemptRepository extends JpaRepository<ShiftClosingAttempt,UUID> {
 Optional<ShiftClosingAttempt> findFirstByCashierShiftIdOrderBySubmittedAtDesc(UUID shiftId);
 List<ShiftClosingAttempt> findByCashierShiftIdOrderBySubmittedAtDesc(UUID shiftId);
}
