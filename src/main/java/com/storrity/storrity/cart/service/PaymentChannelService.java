package com.storrity.storrity.cart.service;

import com.storrity.storrity.cart.dto.PaymentChannelCreateRequest;
import com.storrity.storrity.cart.dto.PaymentChannelUpdateRequest;
import com.storrity.storrity.cart.entity.PaymentChannel;
import com.storrity.storrity.cart.entity.PaymentChannelQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface PaymentChannelService {     
    PaymentChannel create(PaymentChannelCreateRequest request);
    PaymentChannel createCashChannelForStore(UUID storeId);
    PaymentChannel fetch(UUID id);
    PaymentChannel fetch(UUID channelId, UUID storeId);
    List<PaymentChannel> list(PaymentChannelQueryParams params);
    CountDto count(PaymentChannelQueryParams params);
    PaymentChannel update(UUID id, PaymentChannelUpdateRequest request);
    PaymentChannel activate(UUID id);
    PaymentChannel deactivate(UUID id);
}
