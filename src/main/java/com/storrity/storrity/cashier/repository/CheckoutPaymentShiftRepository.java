package com.storrity.storrity.cashier.repository;
import com.storrity.storrity.cart.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CheckoutPaymentShiftRepository extends JpaRepository<CheckoutPayment,UUID> {
 List<CheckoutPayment> findByCashierShiftIdAndPaymentStatus(UUID shiftId,PaymentStatus status);
}
