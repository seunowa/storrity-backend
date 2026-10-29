package com.storrity.storrity.cart.service;

import com.storrity.storrity.cart.dto.ChangeDto;
import com.storrity.storrity.cart.dto.CheckoutCreationDto;
import com.storrity.storrity.cart.entity.Cart;
import com.storrity.storrity.cart.entity.CartStatus;
import com.storrity.storrity.cart.entity.Change;
import com.storrity.storrity.cart.entity.Checkout;
import com.storrity.storrity.cart.entity.CheckoutPayment;
import com.storrity.storrity.cart.entity.CheckoutQueryParams;
import com.storrity.storrity.cart.entity.PaymentChannel;
import com.storrity.storrity.cart.entity.PaymentMethod;
import com.storrity.storrity.cart.entity.PaymentStatus;
import com.storrity.storrity.cart.repository.CartRepositry;
import com.storrity.storrity.cart.repository.ChangeRepository;
import com.storrity.storrity.cart.repository.CheckoutPaymentRepository;
import com.storrity.storrity.cart.repository.CheckoutRepository;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.cashier.entity.CashierShift;
import com.storrity.storrity.cashier.service.CashierShiftService;
import com.storrity.storrity.cashier.service.CasierCashFlowService;
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
import com.storrity.storrity.security.service.AuthenticatedUser;
import com.storrity.storrity.util.exception.BadRequestAppException;
import java.util.ArrayList;
import java.util.Set;
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
    private final SalesService salesService;
    private final PaymentChannelService paymentChannelService;
    private final CheckoutPaymentRepository checkoutPaymentRepository;
    private final ChangeRepository changeRepository;
    private final CashierShiftService cashierShiftService;
    private final CasierCashFlowService cashFlowService;

    @Autowired
    public CheckoutServiceImpl(CheckoutRepository checkoutRepository, CartRepositry cartRepositry,
            SalesService salesService,
            PaymentChannelService paymentChannelService,
            CheckoutPaymentRepository checkoutPaymentRepository, 
            ChangeRepository changeRepository,
            CashierShiftService cashierShiftService,
            CasierCashFlowService cashFlowService) {
        this.checkoutRepository = checkoutRepository;
        this.cartRepositry = cartRepositry;
        this.salesService = salesService;
        this.paymentChannelService = paymentChannelService;
        this.checkoutPaymentRepository = checkoutPaymentRepository;
        this.changeRepository = changeRepository;
        this.cashierShiftService = cashierShiftService;
        this.cashFlowService = cashFlowService;
    }

    @Transactional
    @Override
    public Checkout create(CheckoutCreationDto dto) {
        if (dto == null) {
            throw new BadRequestAppException("Checkout request is required.");
        }
        if(!dto.isPaymentsSumEqualToAmountPaid()){
            throw new BadRequestAppException("Payment Sum is not equals to amount paid");
        }
        
        Cart cart = cartRepositry.findByIdForUpdate(dto.getCartId())
                .orElseThrow(()->new ResourceNotFoundAppException("Cart not found with id: " + dto.getCartId()));
        if(cart.getCartStatus().equals(CartStatus.PAID)){
            throw new BadRequestAppException("Items in cart have already been paid for");
        }
        // Get the currently authenticated username from SecurityContext
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        // The shift is derived from the authenticated cashier and cart store.
        CashierShift shift = cashierShiftService.requireOpenShiftForCheckout(cart.getStore().getId(), username);
        UUID shiftId = shift.getId();

        List<ChangeDto> changeDtos = dto.getChanges() == null ? List.of() : dto.getChanges();
        validateCheckoutAmounts(dto, changeDtos);
        
        // create checkout
        Checkout checkout = Checkout.builder()
                .amountPaid(dto.getAmountPaid())
                .cart(cart)
                .transactionRef(cart.getTransactionRef())
                .shiftId(dto.getShiftId())
                .performedBy(username)
                .build();
        
        checkoutRepository.saveAndFlush(checkout);
        
        List<CheckoutPayment> payments = new ArrayList<>();
        Set<UUID> cashChannelIds = new java.util.HashSet<>();
        for (var p : dto.getPayments()) {
            PaymentChannel channel = paymentChannelService.fetch(p.getPaymentChannelId(), cart.getStore().getId());
            if (isCashChannel(channel)) {
                cashChannelIds.add(channel.getId());
            }
            validateNonNegative(p.getAmount(), "Payment amount");
            validateNonNegative(p.getAmountTendered(), "Amount tendered");
            if (isCashChannel(channel) && p.getAmountTendered().getValueInMicroNaira() < p.getAmount().getValueInMicroNaira()) {
                throw new BadRequestAppException("Cash tendered cannot be less than the cash payment amount.");
            }
            CheckoutPayment payment = CheckoutPayment.builder()
                    .amount(p.getAmount())
                    .amountTendered(p.getAmountTendered())
                    .cartId(cart.getId())
                    .storeId(cart.getStore().getId())
                    .cashierShiftId(shiftId)
                    .checkoutId(checkout.getId())
                    .paymentChannel(channel.getName())
                    .paymentChannelId(channel.getId())
                    .paymentMethod(channel.getPaymentMethod())
                    .paymentReference(p.getPaymentReference())
                    .paymentStatus(PaymentStatus.CONFIRMED)
                    .performedBy(username)
                    .transactionRef(cart.getTransactionRef())
                    .build();
            payments.add(payment);
        }
        checkoutPaymentRepository.saveAllAndFlush(payments); 

        List<Change> changes = new ArrayList<>();
        for (ChangeDto c : changeDtos) {
            PaymentChannel channel = paymentChannelService.fetch(c.getPaymentChannelId(), cart.getStore().getId());
            validateNonNegative(c.getAmount(), "Change amount");
            if (isCashChannel(channel)) {
                cashChannelIds.add(channel.getId());
            }
            Change change = Change.builder()
                    .amount(c.getAmount())
                    .cartId(cart.getId())
                    .storeId(cart.getStore().getId())
                    .cashierShiftId(shiftId)
                    .checkoutId(checkout.getId())
                    .paymentChannel(channel.getName())
                    .paymentChannelId(channel.getId())
                    .paymentMethod(channel.getPaymentMethod())
                    .paymentReference(c.getPaymentReference())
                    .paymentStatus(PaymentStatus.CONFIRMED)
                    .performedBy(username)
                    .transactionRef(cart.getTransactionRef())
                    .build();
            changes.add(change);
        }
        changeRepository.saveAllAndFlush(changes);
        
        // Cash tender and cash change are independent physical drawer movements.
        // A cash change can occur even when the checkout has no CASH payment.
        Money cashTendered = Money.ofMicroNaira(0L);
        Money cashChangeGiven = Money.ofMicroNaira(0L);
        int cashPaymentCount = 0;
        int cashChangeCount = 0;
        for (int i = 0; i < dto.getPayments().size(); i++) {
            CheckoutPayment payment = payments.get(i);
            if (cashChannelIds.contains(payment.getPaymentChannelId())) {
                cashPaymentCount++;
                cashTendered = dto.getPayments().get(i).getAmountTendered();
            }
        }
        for (int i = 0; i < changeDtos.size(); i++) {
            Change change = changes.get(i);
            if (cashChannelIds.contains(change.getPaymentChannelId()) 
                    && change.getPaymentMethod() == PaymentMethod.CASH) {
                cashChangeCount++;
                cashChangeGiven = changeDtos.get(i).getAmount();
            }
        }
        if (cashPaymentCount > 1) {
            throw new BadRequestAppException("A checkout can contain at most one CASH payment.");
        }
        if (cashChangeCount > 1) {
            throw new BadRequestAppException("A checkout can contain at most one CASH change entry.");
        }
        cashFlowService.recordCheckoutCashMovements(
                shift, checkout.getId(), checkout.getTransactionRef(),
                cashTendered, cashChangeGiven, username);
        
        // create sales
        List<SaleCreationDto> items = cart.getItems()
                .stream()
                .map((i)-> SaleCreationDto.builder()
                        .pckQty(i.getPckQty())
//                        .performedBy(principal.getUsername())
                        .productId(i.getProduct().getId())
                        .quantity(i.getQuantity())
                        .taxRate(0.075d)
                        .transactionRef(cart.getTransactionRef())
                        .unitPrice(i.getProduct().getUnitPrice())
//                        @Todo revisit the implementation for discount rate
//                        consider when discound is applied on the product from the products settings and when
//                        the discount is applied at the point of sales
//                        .discountRate()
                        .build())
                .collect(Collectors.toList());
        SalesCreationDto salesCreationDto = SalesCreationDto.builder()
                .performedBy(username)
                .customerId(cart.getCustomerId())
                .clientSystemId(principal.getClientId())
                .clientSystemName(principal.getClientName())
                .transactionRef(cart.getTransactionRef())
                .items(items)
                .build();
        salesService.create(salesCreationDto);
        
//        update care status to paid
        cart.setCartStatus(CartStatus.PAID);
        cartRepositry.save(cart);
        return checkout;
    }

    private void validateCheckoutAmounts(CheckoutCreationDto dto, List<ChangeDto> changes) {
        if (dto.getAmountPaid() == null || dto.getAmountPaid().getValueInMicroNaira() < 0) {
            throw new BadRequestAppException("Amount paid must be zero or positive.");
        }
        if (dto.getPayments() == null || dto.getPayments().isEmpty()) {
            throw new BadRequestAppException("At least one checkout payment is required.");
        }
        for (var payment : dto.getPayments()) {
            if (payment == null || payment.getPaymentChannelId() == null
                    || payment.getAmount() == null || payment.getAmountTendered() == null) {
                throw new BadRequestAppException("Each payment must include a channel, amount and amount tendered.");
            }
        }
        for (ChangeDto change : changes) {
            if (change == null || change.getPaymentChannelId() == null || change.getAmount() == null) {
                throw new BadRequestAppException("Each change entry must include a channel and amount.");
            }
        }
    }

    private void validateNonNegative(Money amount, String label) {
        if (amount == null || amount.getValueInMicroNaira() < 0) {
            throw new BadRequestAppException(label + " must be zero or positive.");
        }
    }

    private boolean isCashChannel(PaymentChannel channel) {
        return PaymentMethod.CASH.equals(channel.getPaymentMethod());
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
