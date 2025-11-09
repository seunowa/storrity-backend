/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cart.service;

import com.storrity.storrity.cart.dto.CartCreationDto;
import com.storrity.storrity.cart.dto.CartDto;
import com.storrity.storrity.cart.dto.CartItemInsertDto;
import com.storrity.storrity.cart.dto.CartUpdateDto;
import com.storrity.storrity.cart.dto.PricedCartDto;
import com.storrity.storrity.cart.entity.Cart;
import com.storrity.storrity.cart.entity.CartItem;
import com.storrity.storrity.cart.entity.CartQueryParams;
import com.storrity.storrity.cart.entity.CartStatus;
import com.storrity.storrity.cart.repository.CartRepositry;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.repository.ProductRepository;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.service.StoreService;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.storrity.storrity.cart.repository.CartItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.ApplicationEventPublisher;

/**
 *
 * @author Seun Owa
 */
@Service
public class CartServiceImpl implements CartService{
    @PersistenceContext
    private EntityManager em;
    private final CartRepositry cartRepositry;
    private final ProductRepository productRepository;
    private final StoreService storeService;
    private final CartItemRepository cartItemRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CartServiceImpl(CartRepositry cartRepositry, ProductRepository productRepository
            , StoreService storeService, CartItemRepository cartItemRepository
            , ApplicationEventPublisher eventPublisher) {
        this.cartRepositry = cartRepositry;
        this.productRepository = productRepository;
        this.storeService = storeService;
        this.cartItemRepository = cartItemRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public PricedCartDto create(CartCreationDto dto) {
        Store store = storeService.fetch(dto.getStoreId());
        
        Cart cart = Cart.builder()
                .transactionRef(dto.getTransactionRef())
                .tag(dto.getTag())
                .store(store)
                .customerId(dto.getCustomerId())
                .cartStatus(CartStatus.AWAITING_PAYMENT)
                .build();
        Cart savedCart = cartRepositry.save(cart);
        return PricedCartDto.from(savedCart);
    }

    @Override
    public PricedCartDto fetch(UUID id) {
        Cart cart = cartRepositry.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Cart not found with id: " + id));        
        return PricedCartDto.from(cart);
    }

    @Override
    public List<CartDto> list(CartQueryParams params) {
        List<Cart> carts = cartRepositry.list(params);
        return carts.stream()
                .map(CartDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public CountDto count(CartQueryParams params) {
        return CountDto
                .builder()
                .count(cartRepositry.countRecords(params))
                .build();
    }
    
    @Transactional
    @Override
    public PricedCartDto update(UUID id, CartUpdateDto dto) {
         Cart cart = cartRepositry.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Cart not found with id: " + id));
         
        if(dto.getCustomerId() != null){
            cart.setCustomerId(dto.getCustomerId());
        } 
        
        Cart savedCart = cartRepositry.save(cart);
        PricedCartDto pricedCartDto = PricedCartDto.from(savedCart);
        
        
//        eventPublisher.publishEvent(new CartUpdatedEvent(savedCart, prevCart));
        return pricedCartDto;
    }

    @Transactional
    @Override
    public PricedCartDto delete(UUID id) {
        Cart cart = cartRepositry.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Cart not found with id: " + id));
        
        PricedCartDto pricedCartDto = PricedCartDto.from(cart);
        
        cartRepositry.delete(cart);
//        eventPublisher.publishEvent(new SupplyDeletedEvent(s));
        return pricedCartDto;
    }

    @Transactional
    @Override
    public PricedCartDto updateCartItems(UUID cartId, CartItemInsertDto dto) {
        
        CartItem cartItem = cartItemRepository
            .findByCartIdAndProductIdForUpdate(cartId, dto.getProductId())
            .orElseGet(() -> createNewCartItem(cartId, dto.getProductId()));
        
        cartItem.setPckQty(dto.getPckQty());
        cartItem.setQuantity(dto.getQuantity());
        
        CartItem savedCartItem = cartItemRepository.save(cartItem);       
        em.flush();
        em.clear();
//        publish event here
        return fetch(cartId);        
    }

    @Transactional
    private CartItem createNewCartItem(UUID cartId, UUID productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundAppException("Product not found with id: " + productId));

        CartItem ci = new CartItem();
        ci.setCartId(cartId);
        ci.setProduct(product);
        ci.setSku(product.getStockKeepingUnit());
        return ci;
    }

    @Transactional
    @Override
    public PricedCartDto deleteCartItem(UUID cartItemId) {
        CartItem cartItem = cartItemRepository.findByIdForUpdate(cartItemId)
                .orElseThrow(()->new ResourceNotFoundAppException("Cart Item not found with id: " + cartItemId));
        UUID cartId = cartItem.getCartId();
        cartItemRepository.deleteById(cartItemId);        
        em.flush();
        em.clear();
        
//        publish event here
        Cart cart = cartRepositry.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundAppException("Cart not found with id: " + cartId));        
        return PricedCartDto.from(cart);
    }
}
