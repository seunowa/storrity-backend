/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.cart.service;

import com.storrity.storrity.cart.entity.PaymentChannel;
import com.storrity.storrity.cart.entity.PaymentMethod;
import com.storrity.storrity.cart.repository.PaymentChannelRepository;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class PaymentChannelServiceImpl implements PaymentChannelService{   

    private static final String CASH_CODE = "CASH";
    private final PaymentChannelRepository paymentChannelRepository;

    @Autowired
    public PaymentChannelServiceImpl(PaymentChannelRepository paymentChannelRepository) {
        this.paymentChannelRepository = paymentChannelRepository;
    }
    
    @Transactional
    @Override
    public PaymentChannel createCashChannelForStore(UUID storeId) {        
        return paymentChannelRepository.save(PaymentChannel.builder()
            .storeId(storeId)
            .paymentMethod(PaymentMethod.CASH)
            .code(CASH_CODE)
            .name("Cash")
            .active(true)
            .build());
    }

    @Override
    public PaymentChannel fetch(UUID id) {
        return paymentChannelRepository.findById(id).orElseThrow(()->{return new ResourceNotFoundAppException("Payment channel not found with id: " + id);});
    }

    @Override
    public PaymentChannel fetch(UUID channelid, UUID storeId) {
        return paymentChannelRepository.findByIdAndStoreIdAndActiveTrue(channelid, storeId).orElseThrow(()->{return new ResourceNotFoundAppException("Payment channel not found with id: " + channelid + " in store: " + storeId);});
    }
}
