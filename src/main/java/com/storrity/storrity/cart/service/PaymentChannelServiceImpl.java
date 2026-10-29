package com.storrity.storrity.cart.service;

import com.storrity.storrity.cart.dto.PaymentChannelCreateRequest;
import com.storrity.storrity.cart.dto.PaymentChannelUpdateRequest;
import com.storrity.storrity.cart.entity.PaymentChannel;
import com.storrity.storrity.cart.entity.PaymentChannelQueryParams;
import com.storrity.storrity.cart.entity.PaymentMethod;
import com.storrity.storrity.cart.repository.PaymentChannelRepository;
import com.storrity.storrity.security.entity.AppUser;
import com.storrity.storrity.security.repository.UserRepository;
import com.storrity.storrity.storeaccess.service.StoreAccessService;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.InputValidationAppException;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final StoreAccessService storeAccessService;
    private final UserRepository userRepository;

    @Autowired
    public PaymentChannelServiceImpl(
            PaymentChannelRepository paymentChannelRepository,
            StoreAccessService storeAccessService,
            UserRepository userRepository) {
        this.paymentChannelRepository = paymentChannelRepository;
        this.storeAccessService = storeAccessService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public PaymentChannel create(PaymentChannelCreateRequest request) {
        requireStoreAccess(request.getStoreId());
        String code = normalizeCode(request.getCode());
        if (paymentChannelRepository.existsByStoreIdAndCode(request.getStoreId(), code)) {
            throw new InputValidationAppException(
                    "A payment channel with code '" + code + "' already exists for this store");
        }
        return paymentChannelRepository.save(PaymentChannel.builder()
                .storeId(request.getStoreId())
                .paymentMethod(request.getPaymentMethod())
                .name(request.getName().trim())
                .code(code)
                .active(true)
                .build());
    }
    
    @Transactional
    @Override
    public PaymentChannel createCashChannelForStore(UUID storeId) { 
        
        return paymentChannelRepository.findByStoreIdAndCode(storeId, CASH_CODE)
                .orElseGet(() -> paymentChannelRepository.save(PaymentChannel.builder()
                        .storeId(storeId)
                        .paymentMethod(PaymentMethod.CASH)
                        .code(CASH_CODE)
                        .name("Cash")
                        .active(true)
                        .build()));
//        return paymentChannelRepository.save(PaymentChannel.builder()
//            .storeId(storeId)
//            .paymentMethod(PaymentMethod.CASH)
//            .code(CASH_CODE)
//            .name("Cash")
//            .active(true)
//            .build());
    }

//    @Override
//    public PaymentChannel fetch(UUID id) {
//        return paymentChannelRepository.findById(id).orElseThrow(()->{return new ResourceNotFoundAppException("Payment channel not found with id: " + id);});
//    }

    @Override
    @Transactional(readOnly = true)
    public PaymentChannel fetch(UUID id) {
        return fetchForManagement(id);
    }

//    @Override
//    public PaymentChannel fetch(UUID channelid, UUID storeId) {
//        return paymentChannelRepository.findByIdAndStoreIdAndActiveTrue(channelid, storeId).orElseThrow(()->{return new ResourceNotFoundAppException("Payment channel not found with id: " + channelid + " in store: " + storeId);});
//    }}

    /** Checkout lookup intentionally returns active channels only. */
    @Override
    @Transactional(readOnly = true)
    public PaymentChannel fetch(UUID channelId, UUID storeId) {
        requireStoreAccess(storeId);
        return paymentChannelRepository.findByIdAndStoreIdAndActiveTrue(channelId, storeId)
                .orElseThrow(() -> new ResourceNotFoundAppException(
                        "Active payment channel not found with id: " + channelId
                                + " in store: " + storeId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentChannel> list(PaymentChannelQueryParams params) {
        requireStoreFilters(params);
        return paymentChannelRepository.list(params);
    }

    @Override
    @Transactional(readOnly = true)
    public CountDto count(PaymentChannelQueryParams params) {
        requireStoreFilters(params);
        return CountDto.builder().count(paymentChannelRepository.countRecords(params)).build();
    }

    @Override
    @Transactional
    public PaymentChannel update(UUID id, PaymentChannelUpdateRequest request) {
        PaymentChannel channel = fetchForManagement(id);
        channel.setName(request.getName().trim());
        return paymentChannelRepository.save(channel);
    }

    @Override
    @Transactional
    public PaymentChannel activate(UUID id) {
        PaymentChannel channel = fetchForManagement(id);
        channel.setActive(true);
        return paymentChannelRepository.save(channel);
    }

    @Override
    @Transactional
    public PaymentChannel deactivate(UUID id) {
        PaymentChannel channel = fetchForManagement(id);
        channel.setActive(false);
        return paymentChannelRepository.save(channel);
    }

    private PaymentChannel fetchForManagement(UUID id) {
        PaymentChannel channel = paymentChannelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundAppException(
                        "Payment channel not found with id: " + id));
        requireStoreAccess(channel.getStoreId());
        return channel;
    }

    private void requireStoreFilters(PaymentChannelQueryParams params) {
        if (params.getStoreIds() == null || params.getStoreIds().isEmpty()) {
            throw new InputValidationAppException(
                    "storeIds must contain at least one store the user is authorized to access");
        }
        params.getStoreIds().forEach(this::requireStoreAccess);
    }

    private void requireStoreAccess(UUID storeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new AccessDeniedException("Authentication is required to access payment channels");
        }
        AppUser user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("Authenticated user was not found"));
        if (!storeAccessService.hasStoreAccess(user.getUsername(), storeId)) {
            throw new AccessDeniedException("User does not have access to store: " + storeId);
        }
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase(Locale.ROOT);
    }
}
