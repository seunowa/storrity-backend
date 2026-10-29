package com.storrity.storrity.cart.service;

import com.storrity.storrity.cart.entity.PaymentChannel;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface PaymentChannelService {
     public PaymentChannel createCashChannelForStore(UUID storeId);
     public PaymentChannel fetch(UUID id);
     public PaymentChannel fetch(UUID channelid, UUID storeId);
}
