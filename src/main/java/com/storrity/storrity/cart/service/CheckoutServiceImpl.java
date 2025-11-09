/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cart.service;

import com.storrity.storrity.cart.dto.CheckoutCreationDto;
import com.storrity.storrity.cart.dto.PricedCartDto;
import com.storrity.storrity.cart.entity.Cart;
import com.storrity.storrity.cart.entity.CartStatus;
import com.storrity.storrity.cart.entity.Checkout;
import com.storrity.storrity.cart.entity.CheckoutQueryParams;
import com.storrity.storrity.cart.repository.CartRepositry;
import com.storrity.storrity.cart.repository.CheckoutRepository;
import com.storrity.storrity.cashaccounts.dto.AccountTransactionInstruction;
import com.storrity.storrity.cashaccounts.dto.Flow;
import com.storrity.storrity.cashaccounts.entity.AccountTransactionStep;
import com.storrity.storrity.cashaccounts.service.AccountTransactionService;
import com.storrity.storrity.sales.dto.SaleCreationDto;
import com.storrity.storrity.sales.dto.SalesCreationDto;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.storrity.storrity.sales.service.SalesService;
import com.storrity.storrity.salesattendant.entity.SalesAttendant;
import com.storrity.storrity.salesattendant.service.SalesAttendantService;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Seun Owa
 */
@Service
public class CheckoutServiceImpl implements CheckoutService{
    
    private final CheckoutRepository checkoutRepository;
    private final CartRepositry cartRepositry;
    private final SalesAttendantService salesAttendantService;
    private final SalesService salesService;
    private final AccountTransactionService accountTransactionService;

    @Autowired
    public CheckoutServiceImpl(CheckoutRepository checkoutRepository, CartRepositry cartRepositry
            , SalesAttendantService salesAttendantService, SalesService salesService
            , AccountTransactionService accountTransactionService) {
        this.checkoutRepository = checkoutRepository;
        this.cartRepositry = cartRepositry;
        this.salesAttendantService = salesAttendantService;
        this.salesService = salesService;
        this.accountTransactionService = accountTransactionService;
    }

    @Transactional
    @Override
    public Checkout create(CheckoutCreationDto dto) {
        Cart cart = cartRepositry.findByIdForUpdate(dto.getCartId()).orElseThrow(()->new ResourceNotFoundAppException("Cart not found with id: " + dto.getCartId()));
        // Get the currently authenticated username from SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SalesAttendant attendant = salesAttendantService.fetchByUsername(username);
        
//        update care status to paid
        cart.setCartStatus(CartStatus.PAID);
        cartRepositry.save(cart);
//        create checkout
        Checkout checkout = Checkout.builder()
                .amountPaid(dto.getAmountPaid())
                .paymentMethod(dto.getPaymentMethod())
                .cart(cart)
                .transactionRef(cart.getTransactionRef())
                .build();
        
        // create sales
        List<SaleCreationDto> items = cart.getItems()
                .stream()
                .map((i)-> SaleCreationDto.builder()
                        .pckQty(i.getPckQty())
                        .performedBy(username)
                        .productId(i.getProduct().getId())
                        .quantity(i.getQuantity())
                        .taxRate(0.075d)
                        .transactionRef(cart.getTransactionRef())
                        .unitPrice(i.getProduct().getUnitPrice())
//                        .discountRate()
                        .build())
                .collect(Collectors.toList());
        SalesCreationDto salesCreationDto = SalesCreationDto.builder()
                .performedBy(username)
                .transactionRef(cart.getTransactionRef())
                .items(items)
                .build();
        salesService.create(salesCreationDto);
        
        
        //debit sales attendants wallet if settings requires it
//        @Todo implelent logic to check if settings for debiting sales attendant wallet is configured before debiting sales attendant wallet 
        PricedCartDto pricedCart = PricedCartDto.from(cart);
        List<AccountTransactionStep> steps = new ArrayList<>();
        steps.add(AccountTransactionStep.builder()
                .accountId(attendant.getSalesWallet())
                .amount(pricedCart.getGrandTotal())
                .flow(Flow.DEBIT)
                .build());
        AccountTransactionInstruction accInstruction = AccountTransactionInstruction.builder()
                .description("sales")
                .performedBy(username)
                .steps(steps)
                .tag("sales")
                .transactionRef(cart.getTransactionRef())
                .build();
        accountTransactionService.create(accInstruction);
        return checkout;
    }

    @Override
    public Checkout fetch(UUID id) {
        return checkoutRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Checkout not found with id: " + id));
    }

    @Override
    public List<Checkout> list(CheckoutQueryParams params) {
        return checkoutRepository.list(params);
    }

    @Override
    public CountDto count(CheckoutQueryParams params) {        
        return CountDto
                .builder()
                .count(checkoutRepository.countRecords(params))
                .build();
    }
    
}
