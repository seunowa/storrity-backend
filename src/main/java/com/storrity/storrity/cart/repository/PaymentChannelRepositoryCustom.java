package com.storrity.storrity.cart.repository;

import com.storrity.storrity.cart.entity.PaymentChannel;
import com.storrity.storrity.cart.entity.PaymentChannelQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface PaymentChannelRepositoryCustom {
    List<PaymentChannel> list(PaymentChannelQueryParams params);
    Long countRecords(PaymentChannelQueryParams params);
}
