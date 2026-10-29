package com.storrity.storrity.cart.repository;

import com.storrity.storrity.cart.entity.PaymentChannel;
import com.storrity.storrity.cart.entity.PaymentMethod;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seun Owa
 */
public interface PaymentChannelRepository
        extends JpaRepository<PaymentChannel, UUID> {

    boolean existsByStoreIdAndCode(UUID storeId, String code);

    List<PaymentChannel> findByStoreIdAndPaymentMethodAndActiveTrue(
        UUID storeId,
        PaymentMethod paymentMethod
    );

    Optional<PaymentChannel> findByIdAndStoreIdAndActiveTrue(
        UUID id,
        UUID storeId
    );
}