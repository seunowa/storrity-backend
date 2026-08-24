/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.stockmovement.service;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.dto.ProductDto;
import com.storrity.storrity.stockmovement.entity.StockMoevmentDirection;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.StockStatus;
import com.storrity.storrity.product.repository.ProductRepository;
import com.storrity.storrity.stockmovement.dto.StockMovementDto;
import com.storrity.storrity.stockmovement.dto.StockMovementInstruction;
import com.storrity.storrity.stockmovement.dto.StockMovementInstructionItem;
import com.storrity.storrity.stockmovement.dto.StockMovementResult;
import com.storrity.storrity.stockmovement.entity.StockMovement;
import com.storrity.storrity.stockmovement.entity.StockMovementQueryParams;
import com.storrity.storrity.stockmovement.entity.StockMovementType;
import com.storrity.storrity.stockmovement.event.StockMovementCreatedEvent;
import com.storrity.storrity.stockmovement.repository.StockMovementRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class StockMovementServiceImpl implements StockMovementService{
    
    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public StockMovementServiceImpl(StockMovementRepository stockMovementRepository, 
            ProductRepository productRepository, ApplicationEventPublisher eventPublisher) {
        this.stockMovementRepository = stockMovementRepository;
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public List<StockMovementDto> create(StockMovementInstruction instruction) {
        Set<UUID> productIds = instruction.getInstructionItems()
                .stream()
                .map((i)->i.getProductId())
                .collect(Collectors.toSet());
        
        Map<UUID, Product> productMap = productRepository
            .findAllByIdForUpdate(productIds)
            .stream()
            .collect(Collectors.toMap(Product::getId, Function.identity()));
        
        LocalDateTime movementAt = LocalDateTime.now();
        List<StockMovement> smList = new ArrayList<>();
        
        for(StockMovementInstructionItem item : instruction.getInstructionItems()){
            if(StockMoevmentDirection.INFLOW.equals(item.getFlow())){
                StockMovement sm = applyInflow(instruction, item, productMap, movementAt);
                smList.add(sm);
            }else if(StockMoevmentDirection.OUTFLOW.equals(item.getFlow())){
                StockMovement sm = applyOutflow(instruction, item, productMap, movementAt);
                smList.add(sm);
            }
        }
        
        List<StockMovement> savedSmList = stockMovementRepository.saveAll(smList);
        List<Product> savedProductList = productRepository.saveAll(productMap.values());
        
        StockMovementResult result = StockMovementResult.builder()
                .description(instruction.getDescription())
                .transactionRef(instruction.getTransactionRef())
                .performedBy(instruction.getPerformedBy())
                .performedAt(LocalDateTime.now())
                .stockMovements(savedSmList
                        .stream()
                        .map(StockMovementDto::from)
                        .collect(Collectors.toList()))
                .products(savedProductList
                        .stream()
                        .map(ProductDto::from)
                        .collect(Collectors.toList()))
                .build();
        
        eventPublisher.publishEvent(new StockMovementCreatedEvent(result));
        return StockMovementDto.from(result);
    }

    @Override
    public StockMovementDto fetch(UUID id) {
        StockMovement sm = stockMovementRepository.findById(id).orElseThrow(()->new ResourceNotFoundAppException("Stock movement not found with id: " + id));  
        return StockMovementDto.from(sm);
    }

    @Override
    public List<StockMovementDto> list(StockMovementQueryParams params) {
        List<StockMovement> stockMovements = stockMovementRepository.list(params);
        
        return stockMovements.stream()
                .map(StockMovementDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public CountDto count(StockMovementQueryParams params) {
        return CountDto
                .builder()
                .count(stockMovementRepository.countRecords(params))
                .build();     
    }
    
    private StockMovement applyInflow(StockMovementInstruction instruction
            , StockMovementInstructionItem item, Map<UUID, Product> productMap, LocalDateTime movementAt){
        Product p = productMap.get(item.getProductId());
        
        if(p == null){
            throw new ResourceNotFoundAppException("Product not found with id: " + item.getProductId());
        }
        
        Double newQtyInStock = p.getQtyInStock() + item.getQuantity();
        p.setQtyInStock(newQtyInStock);
        p.setLastMovementAt(movementAt);
        updateStockStatusAndInventoryValue(p);
        if(instruction.getMovementType() == StockMovementType.SUPPLY){
            p.setLastStockInAt(LocalDateTime.now());
        }
        
        Money movementValue = p.getUnitPrice().multiply(item.getQuantity()); 
        StockMovement sm = StockMovement.builder()
                .balance(newQtyInStock)
                .description(instruction.getDescription())
//                .metadata(metaData)
                .storeId(p.getStore().getId())
                .storeName(p.getStore().getName())
                .performedBy(instruction.getPerformedBy())
                .productId(p.getId())
                .productName(p.getName())
                .productCode(p.getCode())
                .productCategory(p.getCategory())
                .productSubCategory(p.getSubcategory())
                .productBrand(p.getBrand())
                .unitCost(p.getUnitPrice())
                .movementValue(movementValue)
                .qtyIn(item.getQuantity())
                .qtyOut(0d)
                .direction(StockMoevmentDirection.INFLOW)
                .movementType(instruction.getMovementType())
                .baseUnit(p.getStockKeepingUnit())
                .transactionRef(instruction.getTransactionRef())
                .pckQty(item.getPckQty())
                .batchNumber(item.getBatchNumber())
                .expiryDate(item.getExpiryDate())
                .stockStatus(p.getStockStatus())
                .build();

        return sm;
    }
    
    private StockMovement applyOutflow(StockMovementInstruction instruction
            , StockMovementInstructionItem item, Map<UUID, Product> productMap, LocalDateTime movementAt){
        
        Product p = productMap.get(item.getProductId());
        
        if(p == null){
            throw new ResourceNotFoundAppException("Product not found with id: " + item.getProductId());
        }
        
        Double newQtyInStock = p.getQtyInStock() - item.getQuantity();
        if(newQtyInStock < 0){
            throw new IllegalStateException("Insufficient quantity in stock " + p.getName()
                        + ". Required: " + item.getQuantity() + ", Available: " + p.getQtyInStock());
        }
        p.setQtyInStock(newQtyInStock);
        p.setLastMovementAt(movementAt);
        updateStockStatusAndInventoryValue(p);

        Money movementValue = p.getUnitPrice().multiply(item.getQuantity());         
        StockMovement sm = StockMovement.builder()
                .balance(newQtyInStock)
                .description(instruction.getDescription())
//                .metadata(metaData)
                .storeId(p.getStore().getId())
                .storeName(p.getStore().getName())
                .performedBy(instruction.getPerformedBy())
                .productId(p.getId())
                .productName(p.getName())
                .productCode(p.getCode())
                .productCategory(p.getCategory())
                .productSubCategory(p.getSubcategory())
                .productBrand(p.getBrand())
                .unitCost(p.getUnitPrice())
                .movementValue(movementValue)
                .qtyIn(0d)
                .qtyOut(item.getQuantity())
                .direction(StockMoevmentDirection.OUTFLOW)
                .movementType(instruction.getMovementType())
                .baseUnit(p.getStockKeepingUnit())
                .transactionRef(instruction.getTransactionRef())
                .pckQty(item.getPckQty())
                .batchNumber(item.getBatchNumber())
                .expiryDate(item.getExpiryDate())
                .stockStatus(p.getStockStatus())
                .build();

        return sm;
    }
    
    private void updateStockStatusAndInventoryValue(Product p) {
//        Update status
        if (p.getQtyInStock() == null || p.getQtyInStock() <= 0) {
            p.setStockStatus(StockStatus.OUT_OF_STOCK);
            p.setLastStockOutAt(LocalDateTime.now());
        } else if (p.getReorderLevel() != null && p.getQtyInStock() <= p.getReorderLevel()) {
            p.setStockStatus(StockStatus.LOW_STOCK);
        } else if (p.getMaximumStockLevel()!= null && p.getQtyInStock() >= p.getMaximumStockLevel()) {
            p.setStockStatus(StockStatus.HIGH_STOCK);
        } else {
            p.setStockStatus(StockStatus.NORMAL);
        }
        
//        update inventory value
        if(p.getUnitPrice()!= null){
            Double qtyInStock = p.getQtyInStock() == null ? 0d : p.getQtyInStock();
            Money inventoryValue = p.getUnitPrice().multiply(qtyInStock);
            p.setInventoryValue(inventoryValue);
        }
    }
}
