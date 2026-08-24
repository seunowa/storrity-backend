/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.service;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductPackage;
import com.storrity.storrity.product.repository.ProductRepository;
import com.storrity.storrity.security.service.AuthenticatedUser;
import com.storrity.storrity.stockmovement.dto.StockMovementInstruction;
import com.storrity.storrity.stockmovement.dto.StockMovementInstructionItem;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.entity.StockMoevmentDirection;
import com.storrity.storrity.stockmovement.entity.StockMovementType;
import com.storrity.storrity.stockmovement.service.StockMovementService;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.service.StoreService;
import com.storrity.storrity.supply.dto.PurchaseOrderCreationDto;
import com.storrity.storrity.supply.dto.PurchaseOrderItemCreationDto;
import com.storrity.storrity.supply.dto.DeliveryDto;
import com.storrity.storrity.supply.dto.DeliveryItemDto;
import com.storrity.storrity.supply.dto.SupplyDto;
import com.storrity.storrity.supply.dto.SupplyQueryParams;
import com.storrity.storrity.supply.entity.Supply;
import com.storrity.storrity.supply.entity.SupplyAction;
import com.storrity.storrity.supply.entity.SupplyItem;
import com.storrity.storrity.supply.entity.SupplyProcess;
import com.storrity.storrity.supply.entity.SupplyProcessTemplate;
import com.storrity.storrity.supply.entity.SupplyStatus;
import com.storrity.storrity.supply.entity.SupplyTimeline;
import com.storrity.storrity.supply.entity.SupplyTimelineEntry;
import com.storrity.storrity.supply.event.SupplyCreatedEvent;
import com.storrity.storrity.supply.event.SupplyUpdatedEvent;
import com.storrity.storrity.supply.repository.OrderItemRepository;
import com.storrity.storrity.supply.repository.SupplyItemRepository;
import com.storrity.storrity.supply.repository.SupplyRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.supply.entity.OrderItem;
import com.storrity.storrity.util.exception.BadRequestAppException;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class SupplyServiceImpl implements SupplyService{
    
    

    private final StoreService storeService;
    private final SupplyRepository supplyRepository;
    private final SupplyItemRepository supplyItemRepository;  
    private final SupplyProcessSettingsService supplyProcessSettingsService;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final StockMovementService stockMovementService;
    private final ApplicationEventPublisher eventPublisher;

    public SupplyServiceImpl(
            StoreService storeService,
            SupplyItemRepository supplyItemRepository,
            OrderItemRepository orderItemRepository,
            SupplyRepository supplyRepository,
            SupplyProcessSettingsService supplyProcessSettingsService,
            ProductRepository productRepository,
            StockMovementService stockMovementService,
            ApplicationEventPublisher eventPublisher) {

        this.storeService = storeService;
        this.supplyItemRepository = supplyItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.supplyRepository = supplyRepository;
        this.supplyProcessSettingsService = supplyProcessSettingsService;
        this.productRepository = productRepository;
        this.stockMovementService = stockMovementService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public SupplyDto fetch(UUID id) {
        Supply s = supplyRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));
        return SupplyDto.from(s);
    }

    @Override
    public List<SupplyDto> list(SupplyQueryParams params) {
        List<Supply> supplies = supplyRepository.list(params);
        return supplies.stream()
                .map(SupplyDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public CountDto count(SupplyQueryParams params) {
        return CountDto
                .builder()
                .count(supplyRepository.countRecords(params))
                .build();
    }

    @Override
    public SupplyDto delete(UUID id) {
        Supply s = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));
        
//      check prevSupply status and determine if deleting prevSupply is allowed
//      if delete is not allowed throw exception
        checkIfDeleteIsAllowed(s);
        
        supplyRepository.delete(s);
//        eventPublisher.publishEvent(new SupplyDeletedEvent(s));
        return SupplyDto.from(s);
    }

    @Override
    @Transactional
    public SupplyDto createDraft(PurchaseOrderCreationDto dto) {
        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        Store store = storeService.fetch(dto.getStoreId());
        
        SupplyProcess supplyProcess = Optional
        .ofNullable(supplyProcessSettingsService.getSupplyProcessSettings())
        .orElseGet(() -> SupplyProcess.builder()
                .actions(new SupplyProcessTemplate().getSimple())
                .build());

        Supply supply = buildSupply(dto, store, username);
        supply.setSupplyProcess(supplyProcess);

        Supply savedSupply = supplyRepository.save(supply);

        Set<UUID> productIds = dto.getPurchaseOrderItems()
                .stream()
                .map(PurchaseOrderItemCreationDto::getProductId)
                .collect(Collectors.toSet());
        
        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        /*
         * These are the original requested/order items.
         */
        List<OrderItem> orderItems = buildOrderItems(dto, productMap, supply.getId());

        savedSupply.setOrderItems(
                orderItemRepository.saveAll(orderItems)
        );

        eventPublisher.publishEvent(
                new SupplyCreatedEvent(savedSupply)
        );

        return SupplyDto.from(savedSupply);
    }

    @Override
    @Transactional
    public SupplyDto updateDraft(UUID id, PurchaseOrderCreationDto dto) {
        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        
        Supply supply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));
        
        confirmActionIsAllowed(supply, SupplyAction.DRAFT);

        updateDraftProperties(dto, supply, username);
        
        /*
         * Delete any previously staged order items if your
         */
        orderItemRepository.deleteBySupplyId(supply.getId());

        Set<UUID> productIds = dto.getPurchaseOrderItems()
                .stream()
                .map(PurchaseOrderItemCreationDto::getProductId)
                .collect(Collectors.toSet());
        
        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        /*
         * These are the updated requested/order items.
         */
        List<OrderItem> orderItems = buildOrderItems(dto, productMap, supply.getId());
        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItems);

        supply.setOrderItems(savedOrderItems);

        Supply updatedSupply = supplyRepository.save(supply);

        eventPublisher.publishEvent(
                new SupplyUpdatedEvent(updatedSupply, supply)
        );

        return SupplyDto.from(updatedSupply);
    }

    @Override
    @Transactional
    public SupplyDto submitDraft(UUID id) {

        Supply supply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));

        confirmActionIsAllowed(supply, SupplyAction.SUBMIT_DRAFT);
        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        supply.setPurchaseOrderSubmittedAt(LocalDateTime.now());
        supply.setDraftSubmittedBy(username);
        supply.setMostRecentSupplyAction(SupplyAction.SUBMIT_DRAFT);
        
        supply.setSupplyStatus(SupplyStatus.AWAITING_ORDER_APPROVAL);
        
        supply.getSupplyTimeline().appendEntry( SupplyTimelineEntry
                .builder()
                .action(SupplyAction.SUBMIT_DRAFT)
                .performedAt(LocalDateTime.now())
                .performedBy(username)
                .build());

        return SupplyDto.from(
                supplyRepository.save(supply)
        );
    }

    @Override
    @Transactional
    public SupplyDto approveDraft(UUID id) {

        Supply supply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));

        confirmActionIsAllowed(supply, SupplyAction.APPROVE_DRAFT);

        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        supply.setPurchaseOrderApprovedAt(LocalDateTime.now());
        supply.setDraftApprovedBy(username);
        supply.setMostRecentSupplyAction(SupplyAction.APPROVE_DRAFT);

        supply.setSupplyStatus(SupplyStatus.DRAFT_APPROVED);
        
        supply.getSupplyTimeline().appendEntry( SupplyTimelineEntry
                .builder()
                .action(SupplyAction.APPROVE_DRAFT)
                .performedAt(LocalDateTime.now())
                .performedBy(username)
                .build());
        

        return SupplyDto.from(
                supplyRepository.save(supply)
        );
    }

    @Override
    @Transactional
    public SupplyDto order(UUID id) {

        Supply supply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));

        confirmActionIsAllowed(supply, SupplyAction.ORDER);
        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
//        supply.setPurchaseOrderSubmittedAt(LocalDateTime.now());
//        supply.setDraftSubmittedBy(username);
        supply.setMostRecentSupplyAction(SupplyAction.ORDER);

        supply.setSupplyStatus(SupplyStatus.ORDERED);
        
        supply.getSupplyTimeline().appendEntry( SupplyTimelineEntry
                .builder()
                .action(SupplyAction.ORDER)
                .performedAt(LocalDateTime.now())
                .performedBy(username)
                .build());

        return SupplyDto.from(
                supplyRepository.save(supply)
        );
    }

    @Override
    @Transactional
    public SupplyDto deliver(UUID id, DeliveryDto dto) {

        Supply supply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));

        confirmActionIsAllowed(supply, SupplyAction.DELIVER);

        /*
         * Delete any previously staged delivery items if your
         * workflow allows DELIVER to be repeated before approval.
         */
        supplyItemRepository.deleteBySupplyId(supply.getId());

        Set<UUID> productIds = dto.getItems()
                .stream()
                .map(DeliveryItemDto::getProductId)
                .collect(Collectors.toSet());
        
        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        List<SupplyItem> supplyItems =  buildSupplyItemsFromDelivery(dto, productMap, supply.getId());

        List<SupplyItem> savedItems =
                supplyItemRepository.saveAll(supplyItems);

        supply.setSupplyItems(savedItems);

        supply.setDeliveryNoteNumber(
                dto.getDeliveryNoteNumber()
        );

        supply.setInvoiceNumber(
                dto.getInvoiceNumber()
        );

        
        supply.setDeliveredAt(LocalDateTime.now());
        supply.setDeliveryBy(username);
        supply.setMostRecentSupplyAction(SupplyAction.DELIVER);

        supply.setSupplyStatus(
                SupplyStatus.DELIVERED
        );
        
        supply.getSupplyTimeline().appendEntry( SupplyTimelineEntry
                .builder()
                .action(SupplyAction.DELIVER)
                .performedAt(LocalDateTime.now())
                .performedBy(username)
                .build());

        return SupplyDto.from(
                supplyRepository.save(supply)
        );
    }

    @Override
    @Transactional
    public SupplyDto submitDelivery(UUID id) {

        Supply supply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));


        confirmActionIsAllowed(supply, SupplyAction.SUBMIT_DELIVERY);
        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        supply.setDeliverySubmittedAt(LocalDateTime.now());
        supply.setDeliverySbmittedBy(username);
        supply.setMostRecentSupplyAction(SupplyAction.SUBMIT_DELIVERY);

        supply.setSupplyStatus(SupplyStatus.AWAITING_DELIVERY_APPROVAL);
        
        supply.getSupplyTimeline().appendEntry( SupplyTimelineEntry
                .builder()
                .action(SupplyAction.SUBMIT_DELIVERY)
                .performedAt(LocalDateTime.now())
                .performedBy(username)
                .build());

        return SupplyDto.from(supplyRepository.save(supply));
    }

    @Override
    @Transactional
    public SupplyDto approveDelivery(UUID id) {

        Supply supply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));


        confirmActionIsAllowed(supply, SupplyAction.APPROVE_DELIVERY);

        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        supply.setDeliveryApprovedAt(LocalDateTime.now());
        supply.setDeliveryApprovedBy(username);
        supply.setMostRecentSupplyAction(SupplyAction.APPROVE_DELIVERY);

        supply.setSupplyStatus(SupplyStatus.DELIVERY_APPROVED);
        
        supply.getSupplyTimeline().appendEntry( SupplyTimelineEntry
                .builder()
                .action(SupplyAction.APPROVE_DELIVERY)
                .performedAt(LocalDateTime.now())
                .performedBy(username)
                .build());

        return SupplyDto.from(supplyRepository.save(supply));
    }

    @Override
    @Transactional
    public SupplyDto receive(UUID id) {

        Supply supply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));

        confirmActionIsAllowed(supply, SupplyAction.RECEIVE);

        if (supply.getSupplyItems() == null ||
            supply.getSupplyItems().isEmpty()) {
            coppyPurchaceOrderIntoIntoSupplyItems(supply);
        }

        LocalDateTime now = LocalDateTime.now();

        /*
         * 1. Finalize receiving information.
         */
        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        supply.setReceivedAt(now);
        supply.setReceivedBy(username);

        /*
         * 2. Recalculate/validate quantities.
         */
        List<SupplyItem> supplyItems =
                prepareReceivedItems(supply);

        supplyItemRepository.saveAll(supplyItems);

        /*
         * 3. Create inventory stock movement.
         *
         * This is deliberately done BEFORE changing the status.
         */
        StockMovementInstruction instruction =
                buildStockMovementInstruction(
                        supplyItems,
                        supply.getTransactionRef(),
                        username
                );

        stockMovementService.create(instruction);

        /*
         * 4. Only after stock movement succeeds do we
         * mark the supply as RECEIVED.
         */
        supply.setSupplyStatus(SupplyStatus.RECEIVED);
        supply.setMostRecentSupplyAction(SupplyAction.RECEIVE);
        
        supply.getSupplyTimeline().appendEntry( SupplyTimelineEntry
                .builder()
                .action(SupplyAction.RECEIVE)
                .performedAt(LocalDateTime.now())
                .performedBy(username)
                .build());

        Supply savedSupply = supplyRepository.save(supply);

        /*
         * 5. Event is published after the state has been
         * successfully changed.
         */
        eventPublisher.publishEvent(
                new SupplyUpdatedEvent(savedSupply, supply)
        );

        return SupplyDto.from(savedSupply);
    }

    @Override
    @Transactional
    public SupplyDto cancel(UUID id) {

        Supply supply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));

        confirmActionIsAllowed(supply, SupplyAction.CANCEL);

        
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        supply.setCanceledAt(LocalDateTime.now());
        supply.setCancledBy(username);
        supply.setSupplyStatus(SupplyStatus.CANCELED);
        supply.setMostRecentSupplyAction(SupplyAction.CANCEL);
        
        supply.getSupplyTimeline().appendEntry( SupplyTimelineEntry
                .builder()
                .action(SupplyAction.CANCEL)
                .performedAt(LocalDateTime.now())
                .performedBy(username)
                .build());

        return SupplyDto.from(supplyRepository.save(supply));
    }
    
    private List<SupplyItem> prepareReceivedItems(
        Supply supply) {

        Set<UUID> productIds =
                supply.getSupplyItems()
                        .stream()
                        .map(SupplyItem::getProductId)
                        .collect(Collectors.toSet());

        Map<UUID, Product> productMap =
                productRepository.findAllById(productIds)
                        .stream()
                        .collect(Collectors.toMap(
                                Product::getId,
                                Function.identity()
                        ));

        return supply.getSupplyItems()
                .stream()
                .map(item -> {

                    Product product =
                            productMap.get(item.getProductId());

                    if (product == null) {
                        throw new ResourceNotFoundAppException(
                                "Product not found: "
                                + item.getProductId()
                        );
                    }

                    if (item.getQuantityReceived() == null ||
                        item.getQuantityReceived() < 0) {

                        throw new BadRequestAppException(
                                "Invalid quantity received for product "
                                + item.getProductId()
                        );
                    }

                    /*
                     * If package quantities are supplied, you can
                     * validate that they agree with quantityReceived.
                     */
                    if (item.getPckQty() != null &&
                        !item.getPckQty().isEmpty()) {

                        double calculated =
                                computeQtyInBaseUnit(
                                        item.getPckQty(),
                                        product
                                );

                        /*
                         * Depending on your desired semantics,
                         * either set it or validate it.
                         */
                        item.setQuantityReceived(calculated);
                    }

                    return item;
                })
                .collect(Collectors.toList());
    }
    
    private StockMovementInstruction buildStockMovementInstruction(
            List<SupplyItem> supplyItems,
            String transactionRef,
            String performedBy) {

        List<StockMovementInstructionItem> instructionItems =
                supplyItems.stream()
                        .map(this::buildStockMovementItem)
                        .collect(Collectors.toList());

        return StockMovementInstruction.builder()
                .description("Supply received")
                .instructionItems(instructionItems)
                .performedBy(performedBy)
                .transactionRef(transactionRef)
                .movementType(StockMovementType.SUPPLY)
                .build();
    }
    
    private StockMovementInstructionItem buildStockMovementItem(
        SupplyItem supplyItem) {

        return StockMovementInstructionItem.builder()
                .flow(StockMoevmentDirection.INFLOW)
                .quantity(supplyItem.getQuantityReceived())
                .pckQty(
                        supplyItem.getPckQty() == null
                                ? null
                                : new ArrayList<>(supplyItem.getPckQty())
                )
                .productId(supplyItem.getProductId())
//                .productName(supplyItem.getProductName())
//                .productCode(supplyItem.getProductCode())
//                .productCategory(supplyItem.getProductCategory())
//                .productSubCategory(supplyItem.getProductSubCategory())
//                .storeId(supplyItem.getStoreId())
//                .storeName(supplyItem.getStoreName())
                .batchNumber(supplyItem.getBatchNumber())
                .expiryDate(supplyItem.getExpiryDate())
                .build();
    }
    
    private void confirmActionIsAllowed(Supply supply, SupplyAction action) {

        SupplyAction mostRecentAction = supply.getMostRecentSupplyAction();

        // No action history yet.
        // Only DRAFT should be allowed as the first action.
        if (mostRecentAction == null) {
            if (action != SupplyAction.DRAFT) {
                throw new BadRequestAppException(
                        "Action " + action
                        + " cannot be performed. "
                        + "The first action must be DRAFT."
                );
            }
            return;
        }

        // RECEIVED is terminal.
        if (mostRecentAction == SupplyAction.RECEIVE) {
            throw new BadRequestAppException(
                    "Action " + action
                    + " cannot be performed while supply is "
                    + supply.getSupplyStatus()
                    + ". A received supply is closed."
            );
        }

        // CANCELED is terminal.
        if (mostRecentAction == SupplyAction.CANCEL) {
            throw new BadRequestAppException(
                    "Action " + action
                    + " cannot be performed while supply is "
                    + supply.getSupplyStatus()
                    + ". Supply is already canceled."
            );
        }

        // DRAFT can be repeated because the draft remains editable.
        if (mostRecentAction == SupplyAction.DRAFT
                && action == SupplyAction.DRAFT) {
            return;
        }

        // DELIVER can be repeated because delivered quantities
        // can still be edited before delivery is submitted.
        if (mostRecentAction == SupplyAction.DELIVER
                && action == SupplyAction.DELIVER) {
            return;
        }

        List<SupplyAction> actionSequence =
                supply.getSupplyProcess().getActions();

        int currentIndex =
                actionSequence.indexOf(mostRecentAction);

        // The most recent action must belong to the persisted process.
        if (currentIndex < 0) {
            throw new BadRequestAppException(
                    "Supply action " + mostRecentAction
                    + " is not part of the configured supply process."
            );
        }

        int nextIndex = currentIndex + 1;

        if (nextIndex >= actionSequence.size()) {
            throw new BadRequestAppException(
                    "No further action is available for supply."
            );
        }

        SupplyAction expectedAction =
                actionSequence.get(nextIndex);

        if (action != expectedAction) {
            throw new BadRequestAppException(
                    "Action " + action
                    + " cannot be performed while supply is "
                    + supply.getSupplyStatus()
                    + ". Expected action: "
                    + expectedAction
            );
        }
    }

    private Supply buildSupply(PurchaseOrderCreationDto dto, Store store, String username){
        SupplyTimeline timeline = SupplyTimeline.builder().build();
        timeline.appendEntry( SupplyTimelineEntry
                .builder()
                .action(SupplyAction.DRAFT)
                .performedAt(LocalDateTime.now())
                .performedBy(username)
                .build());
        
        Supply supply = Supply.builder()
                .transactionRef(dto.getTransactionRef())
                .storeId(store.getId())
                .storeName(store.getName())
                .expectedSupplyDate(dto.getExpectedSupplyDate())
                .deliveryNoteNumber(dto.getDeliveryNoteNumber())
                .invoiceNumber(dto.getInvoiceNumber())
                .supplierId(dto.getSupplierId())
                .supplierName(dto.getSupplierName())
                .contactPerson(dto.getContactPerson())
                .supplierPhone(dto.getSupplierPhone())
                .supplierEmail(dto.getSupplierEmail())
                .notes(dto.getNotes())
                .grandTotal(computeTotalAmountPayable(dto))
                .createdBy(username)
                .supplyStatus(SupplyStatus.DRAFT)
                .mostRecentSupplyAction(SupplyAction.DRAFT)
                .supplyTimeline(timeline)
                .build();
        
        return supply;
    }
    
    private void updateDraftProperties(PurchaseOrderCreationDto dto, Supply supply, String username){        
        supply.setStoreId(supply.getStoreId());
        supply.setStoreName(supply.getStoreName());
        supply.setExpectedSupplyDate(dto.getExpectedSupplyDate());
        supply.setDeliveryNoteNumber(dto.getDeliveryNoteNumber());
        supply.setInvoiceNumber(dto.getInvoiceNumber());
        supply.setSupplierId(dto.getSupplierId());
        supply.setSupplierName(dto.getSupplierName());
        supply.setContactPerson(dto.getContactPerson());
        supply.setSupplierPhone(dto.getSupplierPhone());
        supply.setSupplierEmail(dto.getSupplierEmail());
        supply.setNotes(dto.getNotes());
        supply.setGrandTotal(computeTotalAmountPayable(dto));
        supply.setCreatedBy(username);
        supply.setSupplyStatus(SupplyStatus.DRAFT);
        supply.setMostRecentSupplyAction(SupplyAction.DRAFT);
        
        LocalDateTime now = LocalDateTime.now();
        supply.getSupplyTimeline().appendEntry( SupplyTimelineEntry
                .builder()
                .action(SupplyAction.DRAFT)
                .performedAt(now)
                .performedBy(username)
                .build());
    }
    
    private Money computeTotalAmountPayable(PurchaseOrderCreationDto dto){
        if (dto.getPurchaseOrderItems() == null || dto.getPurchaseOrderItems().isEmpty()){
            return new Money(0L);
        }
        
        Money totalCostPrice = dto.getPurchaseOrderItems().stream()
            .map(item -> item.getCostPrice())
            .reduce(new Money(0L), Money::add);
    
        return totalCostPrice;
    }
    
    private List<OrderItem> buildOrderItems(PurchaseOrderCreationDto dto, Map<UUID, Product> productMap, UUID supplyId){
        List<OrderItem> supplyItems = dto.getPurchaseOrderItems()
                .stream()
                .map((i)-> { 
                    Product p = productMap.get(i.getProductId());
                    Double computedQty = computeQtyInBaseUnit(i.getPckQty(), p);
                    OrderItem si = OrderItem.builder()
                        .pckQty(i.getPckQty())
//                        .product(p)
                        .productId(p.getId())
                        .productName(p.getName())
                        .productCode(p.getCode())
                        .productCategory(p.getCategory())
                        .productSubCategory(p.getSubcategory())
                        .storeId(p.getStore().getId())
                        .storeName(p.getStore().getName())
                        .quantityOrdered(computedQty)
                        .baseUnit(p.getStockKeepingUnit())
                        .costPrice(i.getCostPrice())
                        .supplyId(supplyId)
                        .build();                    
                    return si;
                })
                .collect(Collectors.toList());
        
        return supplyItems;
    }
    
    private Double computeQtyInBaseUnit(List<PckQty> pckQty, Product product){
        Map<String, ProductPackage> packageMap = product.getPackages()
            .stream()
            .collect(Collectors.toMap(
                ProductPackage::getName,   // key mapper: package name
                Function.identity()        // value mapper: the package itself
            ));
        
        Double qty = pckQty.stream()
            .map((pq)-> {
                ProductPackage pck = packageMap.get(pq.getPackageName());
                if (pck == null) {
                    throw new ResourceNotFoundAppException(
                        "Package with name '" + pq.getPackageName() + "' not found."
                    );
                }
                return pck.getMultiplier() * pq.getQuantity();
            })
            .reduce(0d, Double::sum);
        
        return qty;
    }
    
    private List<SupplyItem> buildSupplyItemsFromDelivery(DeliveryDto dto, Map<UUID, Product> productMap, UUID supplyId){
        List<SupplyItem> supplyItems = dto.getItems()
                .stream()
                .map((i)-> { 
                    Product p = productMap.get(i.getProductId());
                    Double computedQty = computeQtyInBaseUnit(i.getPckQty(), p);
                    SupplyItem si = SupplyItem.builder()
                        .pckQty(i.getPckQty())
                        .productId(p.getId())
                        .productName(p.getName())
                        .productCode(p.getCode())
                        .productCategory(p.getCategory())
                        .productSubCategory(p.getSubcategory())
                        .storeId(p.getStore().getId())
                        .storeName(p.getStore().getName())
                        .quantityReceived(computedQty)
                        .baseUnit(p.getStockKeepingUnit())
                        .costPrice(i.getCostPrice())
                        .supplyId(supplyId)
                        .build();                    
                    return si;
                })
                .collect(Collectors.toList());
        
        return supplyItems;
    }
    
    private void checkIfDeleteIsAllowed(Supply s){
        if(!s.getSupplyStatus().equals(SupplyStatus.DRAFT)){
            throw new BadRequestAppException(
                    "Delete cannot be performed while supply supply status is "
                    + s.getSupplyStatus()
            );
        }
    }
    
    private void coppyPurchaceOrderIntoIntoSupplyItems(Supply supply){
        Collection<OrderItem> orderItems = supply.getOrderItems();
        List<SupplyItem> supplyItems = orderItems.stream().map((o)->{
            return SupplyItem
                    .builder()
                    .baseUnit(o.getBaseUnit())
//                    .batchNumber()
                    .costPrice(o.getCostPrice())
//                    .expiryDate()
                    .orderItemId(o.getId())
                    .pckQty(o.getPckQty())
                    .productCategory(o.getProductCategory())
                    .productCode(o.getProductCode())
                    .productId(o.getProductId())
                    .productName(o.getProductName())
                    .productSubCategory(o.getProductSubCategory())
                    .quantityReceived(o.getQuantityOrdered())
                    .quantityVariance(0d)
                    .storeId(o.getStoreId())
                    .storeName(o.getStoreName())
                    .supplyId(o.getSupplyId())
                    .build();
        }).collect(Collectors.toList());
        
        supply.setSupplyItems(supplyItems);
    }
}
