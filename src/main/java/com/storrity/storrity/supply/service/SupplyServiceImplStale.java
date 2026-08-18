/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.service;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.stockmovement.entity.StockMoevmentDirection;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductPackage;
import com.storrity.storrity.supply.entity.SupplyStatus;
import com.storrity.storrity.product.repository.ProductRepository;
import com.storrity.storrity.stockmovement.dto.StockMovementInstruction;
import com.storrity.storrity.stockmovement.dto.StockMovementInstructionItem;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.entity.StockMovementType;
import com.storrity.storrity.stockmovement.service.StockMovementService;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.service.StoreService;
import com.storrity.storrity.supply.dto.SupplyCreationDtoStale;
import com.storrity.storrity.supply.dto.SupplyDtoStale;
import com.storrity.storrity.supply.dto.SupplyItemCreationDtoStale;
import com.storrity.storrity.supply.dto.SupplyQueryParams;
import com.storrity.storrity.supply.dto.SupplyStatusUpdateDtoStale;
import com.storrity.storrity.supply.dto.SupplyUpdateDtoStale;
import com.storrity.storrity.supply.entity.Supply;
import com.storrity.storrity.supply.entity.SupplyAction;
import com.storrity.storrity.supply.entity.SupplyItem;
import com.storrity.storrity.supply.event.SupplyCreatedEvent;
import com.storrity.storrity.supply.event.SupplyUpdatedEvent;
import com.storrity.storrity.supply.repository.SupplyItemRepository;
import com.storrity.storrity.supply.repository.SupplyRepository;
import com.storrity.storrity.util.approval.ApprovalResponse;
import com.storrity.storrity.util.approval.ApprovalStatus;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.BadRequestAppException;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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
public class SupplyServiceImplStale implements SupplyServiceStale{
    
    private final StoreService storeService;
    private final SupplyRepository supplyRepository;
    private final SupplyItemRepository supplyItemRepository;
    private final ProductRepository productRepository;
    private final StockMovementService stockMovementService;
    private final ApplicationEventPublisher eventPublisher;

    public SupplyServiceImplStale(StoreService storeService, SupplyItemRepository supplyItemRepository
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
    public SupplyDtoStale create(SupplyCreationDtoStale dto) {
        Store store = storeService.fetch(dto.getStoreId());        
        
        Supply supply = buildSupply(dto, store);
        
        Supply savedSupply = supplyRepository.save(supply);
        
        Set<UUID> productIds = dto.getItems()
                .stream()
                .map(SupplyItemCreationDtoStale::getProductId)
                .collect(Collectors.toSet());
        
        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        
        List<SupplyItem> supplyItems = buildSupplyItem(dto, supply, productMap);
        
        List<SupplyItem> savedSupplyItems = supplyItemRepository.saveAll(supplyItems);
        
        savedSupply.setSupplyItems(savedSupplyItems);       
        
        eventPublisher.publishEvent(new SupplyCreatedEvent(savedSupply));
        return SupplyDtoStale.from(savedSupply);
    }

    @Override
    public SupplyDtoStale fetch(UUID id) {
        Supply s = supplyRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));
        return SupplyDtoStale.from(s);
    }

    @Override
    public List<SupplyDtoStale> list(SupplyQueryParams params) {
        List<Supply> supplies = supplyRepository.list(params);
        return supplies.stream()
                .map(SupplyDtoStale::from)
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
    public SupplyDtoStale update(UUID id, SupplyUpdateDtoStale dto) {
        
        Supply prevSupply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));
        // check prevSupply status and determin if it can be updated to the new status in the update dto
        checkIfMutationAllowed(prevSupply);
        
        Store store = storeService.fetch(dto.getStoreId());
        // update prevSupply propoerties excluding prevSupply items which will be updataed down the line
        updateProperties(dto, prevSupply, store);
//        prevSupply.getItems().clear();
        
        Supply savedSupply = supplyRepository.save(prevSupply);
        
        // Delete previous prevSupply items        
//        List<UUID> prevSupplyItemIds = prevSupply.getItems().stream()
//                    .map((i)->i.getId()).collect(Collectors.toList());
//        supplyItemRepository.deleteAllById(prevSupplyItemIds);
//        supplyItemRepository.flush();
        supplyItemRepository.deleteBySupplyId(prevSupply.getId());
        
        // build and save new supply items
        Set<UUID> productIds = dto.getItems()
                .stream()
                .map(SupplyItemCreationDtoStale::getProductId)  
                .collect(Collectors.toSet());
        
        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        
        List<SupplyItem> supplyItems = buildSupplyItem(dto, prevSupply, productMap);
        
        List<SupplyItem> savedSupplyItems = supplyItemRepository.saveAll(supplyItems);        
        
        savedSupply.setSupplyItems(savedSupplyItems);
        
        eventPublisher.publishEvent(new SupplyUpdatedEvent(savedSupply, prevSupply));
        return SupplyDtoStale.from(savedSupply);
    }

    @Transactional
    @Override
    public SupplyDtoStale delete(UUID id) {
        Supply s = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));
        
//      check prevSupply status and determine if deleting prevSupply is allowed
//      if delete is not allowed throw exception
        checkIfMutationAllowed(s);
        
        supplyRepository.delete(s);
//        eventPublisher.publishEvent(new SupplyDeletedEvent(s));
        return SupplyDtoStale.from(s);
    }
    
    @Transactional
    @Override
    @Deprecated
//    @Todo comsider properly how orders transition formm one state to the other and the controller methods (API endpoints) to support this
    public SupplyDtoStale updateStatus(UUID id, SupplyStatusUpdateDtoStale dto) {
        Supply supply = supplyRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Supply not found with id: " + id));
        // check prevSupply status and determin if it can be updated to the new status in the update dto
        checkIfMutationAllowed(supply);
        
        
        if(SupplyStatus.RECEIVED.equals(dto.getSupplyStatus())){
            LocalDateTime now = LocalDateTime.now();
            supply.setReceivedAt(now);
//            supply.setReceivedDate(now.toLocalDate());
            
//            Update QuantityReceived and QuantityRejected
            Set<UUID> productIds = supply.getSupplyItems()
                .stream()
                .map(SupplyItem::getProductId)
                .collect(Collectors.toSet());
            Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
            
            List<SupplyItem> supplyItems = supply.getSupplyItems()
                .stream()
                .map((i)-> {   
//                    @Todo revisit the implementation here and ensure it works currectly without introducing complexity to the api
                    Product p = productMap.get(i.getProductId());
                    Double qtyReceived = computeQtyInSKU(i.getPckQty(), p);
//                    Double tempQtyRejected = i.getQuantityOrdered() - qtyReceived;
//                    Double qtyRejected = tempQtyRejected > 0 ? tempQtyRejected : 0;
                    
                    i.setQuantityReceived(qtyReceived);
//                    i.setQuantityVariance(qtyRejected);                    
                    return i;
                })
                .collect(Collectors.toList());            
            
            List<SupplyItem> savedSupplyItems = supplyItemRepository.saveAll(supplyItems);
            //Create stock movement
            StockMovementInstruction smInstruction = buildStockMovementInstruction(savedSupplyItems, supply.getTransactionRef(), "");
            stockMovementService.create(smInstruction);
        }

        supply.setSupplyStatus(dto.getSupplyStatus());
        Supply savedSupply = supplyRepository.save(supply);
        return SupplyDtoStale.from(savedSupply);
    }
    
//    public ApprovalResponse requestApproval(UUID id){
//    }
//    
//    public ApprovalResponse approve(UUID id){
//    }
    
    private Supply buildSupply(SupplyCreationDtoStale dto, Store store){
        Supply supply = Supply.builder()
                .transactionRef(dto.getTransactionRef())
//                .store(store)
                .storeId(store.getId())
                .storeName(store.getName())
                .expectedSupplyDate(dto.getExpectedSupplyDate())
                .draftSubmittedBy(dto.getEnteredByUserId())
                .receivedBy(dto.getReceivedByUserId())
                .deliveryApprovedBy(dto.getApprovedByUserId())
                .deliveryNoteNumber(dto.getDeliveryNoteNumber())
                .invoiceNumber(dto.getInvoiceNumber())
                .supplierId(dto.getSupplierId())
                .supplierName(dto.getSupplierName())
                .contactPerson(dto.getContactPerson())
                .supplierPhone(dto.getSupplierPhone())
                .supplierEmail(dto.getSupplierEmail())
                .notes(dto.getNotes())
                .grandTotal(computeTotalAmountPayable(dto))
                .supplyStatus(SupplyStatus.DRAFT)
                .mostRecentSupplyAction(SupplyAction.DRAFT)
                .build();
        
        
        return supply;
    }
    
    private List<SupplyItem> buildSupplyItem(SupplyCreationDtoStale dto, Supply supply, Map<UUID, Product> productMap){
        
        
        List<SupplyItem> supplyItems = dto.getItems()
                .stream()
                .map((i)-> { 
                    Product p = productMap.get(i.getProductId());
                    Double computedQty = computeQtyInSKU(i.getPckQty(), p);
                    SupplyItem si = SupplyItem.builder()
                        .batchNumber(i.getBatchNumber())
                        .expiryDate(i.getExpiryDate())
                        .pckQty(i.getPckQty())
//                        .product(p)
                        .productId(p.getId())
                        .productName(p.getName())
                        .productCode(p.getCode())
                        .productCategory(p.getCategory())
                        .productSubCategory(p.getSubcategory())
                        .storeId(p.getStore().getId())
                        .storeName(p.getStore().getName())
                        .quantityReceived(computedQty)
//                        .quantityOrdered(computedQty)
                        .baseUnit(p.getStockKeepingUnit())
                        .costPrice(i.getCostPrice())
                        .supplyId(supply.getId())
                        .build();                    
                    return si;
                })
                .collect(Collectors.toList());
        
        return supplyItems;
    }
    
    private Supply updateProperties(SupplyUpdateDtoStale dto, Supply supply, Store store){
        
        supply.setTransactionRef(dto.getTransactionRef());
        //supply.setStore(store);
        supply.setStoreId(store.getId());
        supply.setStoreName(store.getName());
        supply.setExpectedSupplyDate(dto.getExpectedSupplyDate());
//        supply.setSupplyStatus(dto.getSupplyStatus());
        supply.setDeliveryNoteNumber(dto.getDeliveryNoteNumber());
        supply.setInvoiceNumber(dto.getInvoiceNumber());
        supply.setSupplierId(dto.getSupplierId());
        supply.setSupplierName(dto.getSupplierName());
        supply.setContactPerson(dto.getContactPerson());
        supply.setSupplierPhone(dto.getSupplierPhone());
        supply.setSupplierEmail(dto.getSupplierEmail());
        supply.setNotes(dto.getNotes());
        supply.setGrandTotal(computeTotalAmountPayable(dto));
        return supply;
    }
    
    private Double computeQtyInSKU(List<PckQty> pckQty, Product product){
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
    
    private Money computeTotalAmountPayable(SupplyCreationDtoStale dto){
        if (dto.getItems() == null || dto.getItems().isEmpty()){
            return new Money(0L);
        }
        
        Money totalCostPrice = dto.getItems().stream()
            .map(item -> item.getCostPrice())
            .reduce(new Money(0L), Money::add);
    
        return totalCostPrice;
    }
    
    private StockMovementInstruction buildStockMovementInstruction(List<SupplyItem> supplyItems, String transRef, String performedBy){
                
        List<StockMovementInstructionItem> instructionItems = supplyItems
                .stream()
                .map((s)->{
                    List<PckQty> qtyList = new ArrayList<>(s.getPckQty());
                    StockMovementInstructionItem item = StockMovementInstructionItem
                            .builder()
                            .flow(StockMoevmentDirection.INFLOW)
                            .quantity(s.getQuantityReceived())
                            .pckQty(qtyList)
                            .productId(s.getProductId())
                            .productName(s.getProductName())
                            .productCode(s.getProductCode())
                            .productCategory(s.getProductCategory())
                            .productSubCategory(s.getProductSubCategory())
                            .storeId(s.getStoreId())
                            .storeName(s.getStoreName())
                            .build();
                    return item;})
                .collect(Collectors.toList());
        
        StockMovementInstruction smInstruction = StockMovementInstruction.builder()
                .description("supply")
                .instructionItems(instructionItems)
                .performedBy(performedBy)
                .transactionRef(transRef)
                .movementType(StockMovementType.SUPPLY)
                .build();
        
        return smInstruction;
    }
    
    private void checkIfMutationAllowed(Supply s){
        if(SupplyStatus.RECEIVED.equals(s.getSupplyStatus())){
           throw new  BadRequestAppException("Supply status is RECEIVED it can not be deleted");
        }
    } 
}
