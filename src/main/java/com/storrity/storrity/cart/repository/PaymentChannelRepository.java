package com.storrity.storrity.cart.repository;

import com.storrity.storrity.cart.entity.PaymentChannel;
import com.storrity.storrity.cart.entity.PaymentMethod;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Seun Owa
 */
public interface PaymentChannelRepository
        extends JpaRepository<PaymentChannel, UUID>, PaymentChannelRepositoryCustom {

    boolean existsByStoreIdAndCode(UUID storeId, String code);
    
    boolean existsByStoreIdAndPaymentMethod(UUID storeId, PaymentMethod paymentMethod);

    boolean existsByStoreIdAndCodeAndIdNot(UUID storeId, String code, UUID id);
    
    @Query("""
        SELECT pc
        FROM PaymentChannel pc
        WHERE pc.storeId = :storeId
          AND pc.paymentMethod = com.storrity.storrity.cart.entity.PaymentMethod.CASH
        """)
    Optional<PaymentChannel> findCashChannelByStoreId(UUID storeId);

    List<PaymentChannel> findByStoreIdAndPaymentMethodAndActiveTrue(
        UUID storeId,
        PaymentMethod paymentMethod
    );

    Optional<PaymentChannel> findByIdAndStoreIdAndActiveTrue(
        UUID id,
        UUID storeId
    );

    Optional<PaymentChannel> findByStoreIdAndCode(UUID storeId, String code);
}