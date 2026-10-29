package com.storrity.storrity.cashier.repository;

import com.storrity.storrity.cashier.entity.*;
import java.util.*;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface CashierShiftRepository extends JpaRepository<CashierShift, UUID> {

    boolean existsByStoreIdAndCashierUsernameAndStatusIn(UUID storeId, String username, Collection<CashierShiftStatus> statuses);

    Optional<CashierShift> findFirstByStoreIdAndCashierUsernameAndStatusOrderByOpenedAtDesc(UUID storeId, String username, CashierShiftStatus status);

    List<CashierShift> findByStoreIdAndStatusOrderByOpenedAtDesc(UUID storeId, CashierShiftStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from CashierShift s where s.id=:id")
    Optional<CashierShift> findByIdForUpdate(@Param("id") UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from CashierShift s where s.storeId=:storeId and s.cashierUsername=:username and s.status=:status")
    Optional<CashierShift> findOpenForUpdate(@Param("storeId") UUID storeId, @Param("username") String username, @Param("status") CashierShiftStatus status);
}
