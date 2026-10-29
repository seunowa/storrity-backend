package com.storrity.storrity.cashier.repository;
import com.storrity.storrity.cashier.entity.CashFlow;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CashFlowRepository extends JpaRepository<CashFlow,UUID> {
 List<CashFlow> findByCashierShiftIdOrderByCreatedAtAsc(UUID shiftId);
 boolean existsBySourceReferenceAndFlowType(String sourceReference, com.storrity.storrity.cashier.entity.CashFlowType flowType);
}
