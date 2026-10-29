package com.storrity.storrity.cart.repository;

import com.storrity.storrity.cart.entity.CheckoutPayment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seun Owa
 */
public interface CheckoutPaymentRepository extends JpaRepository<CheckoutPayment, UUID> {
    List<CheckoutPayment> findByCheckoutId(UUID checkoutId);
    List<CheckoutPayment> findByCartId(UUID cartId);
}