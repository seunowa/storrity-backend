package com.storrity.storrity.cart.repository;

import com.storrity.storrity.cart.entity.Change;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seun Owa
 */
public interface ChangeRepository extends JpaRepository<Change, UUID> {
    List<Change> findByCheckoutId(UUID checkoutId);
    List<Change> findByCartId(UUID cartId);
}