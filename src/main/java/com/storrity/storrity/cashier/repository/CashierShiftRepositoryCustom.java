package com.storrity.storrity.cashier.repository;

import com.storrity.storrity.cashier.entity.CashierShift;
import com.storrity.storrity.cashier.entity.CashierShiftQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface CashierShiftRepositoryCustom {

    List<CashierShift> list(CashierShiftQueryParams params);

    Long countRecords(CashierShiftQueryParams params);
}