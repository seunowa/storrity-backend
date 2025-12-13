/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.service;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.dto.StockFlow;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductPackage;
import com.storrity.storrity.product.entity.SupplyStatus;
import com.storrity.storrity.product.repository.ProductRepository;
import com.storrity.storrity.stockmovement.dto.StockMovementInstruction;
import com.storrity.storrity.stockmovement.dto.StockMovementInstructionItem;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.service.StockMovementService;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.service.StoreService;
import com.storrity.storrity.supply.dto.SupplyCreationDto;
import com.storrity.storrity.supply.dto.SupplyDto;
import com.storrity.storrity.supply.dto.SupplyItemCreationDto;
import com.storrity.storrity.supply.dto.SupplyQueryParams;
import com.storrity.storrity.supply.dto.SupplyUpdateDto;
import com.storrity.storrity.supply.entity.Supply;
import com.storrity.storrity.supply.entity.SupplyItem;
import com.storrity.storrity.supply.event.SupplyCreatedEvent;
import com.storrity.storrity.supply.event.SupplyUpdatedEvent;
import com.storrity.storrity.supply.repository.SupplyItemRepository;
import com.storrity.storrity.supply.repository.SupplyRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.BadRequestAppException;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ProductRepository productRepository;
    private final StockMovementService stockMovementService;
    private final ApplicationEventPublisher eventPublisher;

    public SupplyServiceImpl(StoreService storeService, SupplyItemRepository supplyItemRepository
            , SupplyRepository supplyRepository, ProductRepository productRepository
            ,StockMovementService stockMovementService, ApplicationEventPublisher eventPublisher) {
        this.storeService = storeService;
        this.supplyRepository = supplyRepository;
        this.supplyItemRepository = supplyItemRepository;
        this.productRepository = productRepository;
        this.stockMovementService = stockMovementService;
        this.eventPublisher = eventPublisher;
    }    

    @Transactional
    @Override
    public SupplyDto create(SupplyCreationDto dto) {
        Store store = storeService.fetch(dto.getStoreId());        
        
        Supply supply = buildSupply(dto, store);
        
        Supply savedSupply = supplyRepository.save(supply);
        
        Set<UUID> productIds = dto.getItems()
                .stream()
                .map(SupplyItemCreationDto::getProductId)
                .collect(Collectors.toSet());
        
        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        
        List<SupplyItem> supplyItems = buildSupplyItem(dto, supply, productMap);
        
        List<SupplyItem> savedSupplyItems = supplyItemRepository.saveAll(supplyItems);
        
        savedSupply.setItems(savedSupplyItems);
        
//      if supply status is RECEIVED create stock movement
        if(SupplyStatus.RECEIVED.equals(dto.getSupplyStatus())){
            StockMovementInstruction smInstruction = buildStockMovementInstruction(supplyItems, dto);
            stockMovementService.create(smInstruction);
        }        
        
        eventPublisher.publishEvent(new SupplyCreatedEvent(savedSupply));
        return SupplyDto.from(savedSupply);
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

    @Transactional
    @Override
    public SupplyDto update(UUID id, SupplyUpdateDto dto) {
        
        Supply prevSsupply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));
        // check prevSsupply status and determin if it can be updated to the new status in the update dto
        checkIfMutationAllowed(prevSsupply);
        
        Store store = storeService.fetch(dto.getStoreId());
        // update prevSsupply propoerties excluding prevSsupply items which will be updataed down the line
        updateProperties(dto, prevSsupply, store);        
        Supply savedSupply = supplyRepository.save(prevSsupply);
        
        // Delete previous prevSsupply items        
        List<UUID> prevSupplyItemIds = prevSsupply.getItems().stream()
                    .map((i)->i.getId()).collect(Collectors.toList());
        supplyItemRepository.deleteAllById(prevSupplyItemIds);        
        
        // build and save new prevSsupply items
        Set<UUID> productIds = dto.getItems()
                .stream()
                .map(SupplyItemCreationDto::getProductId)  
                .collect(Collectors.toSet());
        
        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        
        List<SupplyItem> supplyItems = buildSupplyItem(dto, prevSsupply, productMap);
        
        List<SupplyItem> savedSupplyItems = supplyItemRepository.saveAll(supplyItems);        
        
        savedSupply.setItems(savedSupplyItems);       
        
//      if supply status is RECEIVED create stock movement
        if(SupplyStatus.RECEIVED.equals(dto.getSupplyStatus())){
            StockMovementInstruction smInstruction = buildStockMovementInstruction(supplyItems, dto);
            stockMovementService.create(smInstruction);
        }
        
        eventPublisher.publishEvent(new SupplyUpdatedEvent(savedSupply, prevSsupply));
        return SupplyDto.from(savedSupply);
    }

    @Transactional
    @Override
    public SupplyDto delete(UUID id) {
        Supply s = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));
        
//      check prevSsupply status and determine if deleting prevSsupply is allowed
//      if delete is not allowed throw exception
        checkIfMutationAllowed(s);
        
        supplyRepository.delete(s);
//        eventPublisher.publishEvent(new SupplyDeletedEvent(s));
        return SupplyDto.from(s);
    }
    
    private Supply buildSupply(SupplyCreationDto dto, Store store){
        Supply supply = Supply.builder()
                .transactionRef(dto.getTransactionRef())
                .store(store)
                .supplyDate(dto.getSupplyDate())
                .enteredByUserId(dto.getEnteredByUserId())
                .receivedByUserId(dto.getReceivedByUserId())
                .approvedByUserId(dto.getApprovedByUserId())
                .supplyStatus(dto.getSupplyStatus())
                .deliveryNoteNumber(dto.getDeliveryNoteNumber())
                .invoiceNumber(dto.getInvoiceNumber())
                .supplierId(dto.getSupplierId())
                .supplierName(dto.getSupplierName())
                .contactPerson(dto.getContactPerson())
                .supplierPhone(dto.getSupplierPhone())
                .supplierEmail(dto.getSupplierEmail())
                .deliveryFee(dto.getDeliveryFee())
                .paymentMethod(dto.getPaymentMethod())
                .amountPaid(dto.getAmountPaid())
                .notes(dto.getNotes())
                .grandTotal(computeTotalAmountPayable(dto))
                .build();
        return supply;
    }
    
    private List<SupplyItem> buildSupplyItem(SupplyCreationDto dto, Supply supply, Map<UUID, Product> productMap){
        
        List<SupplyItem> supplyItems = dto.getItems()
                .stream()
                .map((i)-> { 
                    Product p = productMap.get(i.getProductId());
                    return SupplyItem.builder()
                        .batchNumber(i.getBatchNumber())
                        .expiryDate(i.getExpiryDate())
                        .pckQty(i.getPckQty())
                        .product(p)
                        .quantity(computeQtyInSKU(i, p))
                        .sku(p.getStockKeepingUnit())
                        .costPrice(i.getCostPrice())
                        .supplyId(supply.getId())
                        .build();
                })
                .collect(Collectors.toList());
        
        return supplyItems;
    }
    
    private Supply updateProperties(SupplyUpdateDto dto, Supply supply, Store store){
        
        supply.setTransactionRef(dto.getTransactionRef());
        supply.setStore(store);
        supply.setSupplyDate(dto.getSupplyDate());
        supply.setSupplyStatus(dto.getSupplyStatus());
        supply.setDeliveryNoteNumber(dto.getDeliveryNoteNumber());
        supply.setInvoiceNumber(dto.getInvoiceNumber());
        supply.setSupplierId(dto.getSupplierId());
        supply.setSupplierName(dto.getSupplierName());
        supply.setContactPerson(dto.getContactPerson());
        supply.setSupplierPhone(dto.getSupplierPhone());
        supply.setSupplierEmail(dto.getSupplierEmail());
        supply.setDeliveryFee(dto.getDeliveryFee());
        supply.setPaymentMethod(dto.getPaymentMethod());
        supply.setAmountPaid(dto.getAmountPaid());
        supply.setNotes(dto.getNotes());
        supply.setEnteredByUserId(dto.getEnteredByUserId());
        supply.setReceivedByUserId(dto.getReceivedByUserId());
        supply.setApprovedByUserId(dto.getApprovedByUserId());
        supply.setGrandTotal(computeTotalAmountPayable(dto));
        
        return supply;
    }
    
    private Double computeQtyInSKU(SupplyItemCreationDto dto, Product product){
        Map<String, ProductPackage> packageMap = product.getPackages()
            .stream()
            .collect(Collectors.toMap(
                ProductPackage::getName,   // key mapper: package name
                Function.identity()        // value mapper: the package itself
            ));
        
        Double qty = dto.getPckQty().stream()
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
    
    private Money computeTotalAmountPayable(SupplyCreationDto dto){
        if (dto.getItems() == null || dto.getItems().isEmpty()){
            return new Money(0L);
        }
        
        Money totalCostPrice = dto.getItems().stream()
            .map(item -> item.getCostPrice())
            .reduce(new Money(0L), Money::add);
    
        return totalCostPrice;
    }
    
    private StockMovementInstruction buildStockMovementInstruction(List<SupplyItem> supplyItems, SupplyCreationDto dto){
        
        List<StockMovementInstructionItem> instructionItems = supplyItems
                .stream()
                .map((s)->{
                    List<PckQty> qtyList = new ArrayList<>(s.getPckQty());
                    return new StockMovementInstructionItem(s.getQuantity(), StockFlow.INFLOW, s.getProduct().getId(), qtyList);})
                .collect(Collectors.toList());
        
        StockMovementInstruction smInstruction = StockMovementInstruction.builder()
                .description("supply")
                .instructionItems(instructionItems)
                .performedBy(dto.getPerformedBy())
                .transactionRef(dto.getTransactionRef())                
                .build();
        
        return smInstruction;
    }
    
    private void checkIfMutationAllowed(Supply s){
        if(SupplyStatus.RECEIVED.equals(s.getSupplyStatus())){
           throw new  BadRequestAppException("Supply status is RECEIVED it can not be deleted");
        }        
        
        if(SupplyStatus.RETURNED.equals(s.getSupplyStatus())){
           throw new  BadRequestAppException("Supply status is RETURNED it can not be deleted");
        }
    }
}
