/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.service;

import com.storrity.storrity.product.dto.StockFlow;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.repository.ProductRepository;
import com.storrity.storrity.product.service.ProductService;
import com.storrity.storrity.sales.dto.SaleCreationDto;
import com.storrity.storrity.sales.dto.SaleDto;
import com.storrity.storrity.sales.dto.SalesCreationDto;
import com.storrity.storrity.sales.dto.SalesCreationResponse;
import com.storrity.storrity.sales.entity.Sale;
import com.storrity.storrity.sales.entity.SaleQueryParams;
import com.storrity.storrity.sales.repository.SaleRepository;
import com.storrity.storrity.stockmovement.dto.StockMovementInstruction;
import com.storrity.storrity.stockmovement.dto.StockMovementInstructionItem;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.service.StockMovementService;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.dto.ItemPrice;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
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
public class SalesServiceImpl implements SalesService{
    
    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final StockMovementService stockMovementService;
    private final ApplicationEventPublisher eventPublisher;
    
    @Autowired
    public SalesServiceImpl(SaleRepository saleRepository, ProductService productService, ProductRepository productRepository
            ,StockMovementService stockMovementService, ApplicationEventPublisher eventPublisher) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.stockMovementService = stockMovementService;
        this.eventPublisher = eventPublisher;
    }
    

//    @Transactional
//    @Override
//    public SaleDto create(SaleCreationDto dto) {
//        
//        Product p = productService.fetchRaw(dto.getProductId());
//        ItemPrice<SaleCreationDto> itemPrice = 
//                new ItemPrice<>(dto,
//                        dto.getUnitPrice(),
//                        dto.getDiscountRate(),
//                        dto.getQuantity(),
//                        dto.getTaxRate());
//        
//        
//        Sale sale = Sale.builder()
//                .discountRate(dto.getDiscountRate())
//                .pckQty(dto.getPckQty())
//                .performedBy(dto.getPerformedBy())
//                .product(p)
//                .quantity(dto.getQuantity())
//                .sku(p.getStockKeepingUnit())
//                .store(p.getStore())
//                .taxRate(dto.getTaxRate())
//                .transactionRef(dto.getTransactionRef())
//                .unitPrice(dto.getUnitPrice())
//                .preDiscountPrice(itemPrice.getPreDiscountAmount())
//                .discountAmount(itemPrice.getDiscountAmount())
//                .discountRate(itemPrice.getDiscountRate())
//                .discountedAmount(itemPrice.getDiscountedPrice())
//                .taxAmount(itemPrice.getTaxAmount())
//                .taxRate(itemPrice.getTaxRate())
//                .amount(itemPrice.getSum())
//                .build();
//        
//        Sale savedSale = saleRepository.save(sale);
//        //create stock movement
//        StockMovementInstruction smInstruction = StockMovementInstruction.builder()
//                .description("sales")
//                .instructionItems(List.of(new StockMovementInstructionItem(dto.getQuantity(), StockFlow.OUTFLOW, p.getId(), dto.getPckQty())))
//                .performedBy(dto.getPerformedBy())
//                .transactionRef(dto.getTransactionRef())                
//                .build();
//        stockMovementService.create(smInstruction);
//        //publish sale creation event
//        return SaleDto.from(savedSale);
//    }

    @Transactional
    @Override
    public SalesCreationResponse create(SalesCreationDto dto) {
        //@Todo refactor implementation such that cost price is not required to create sales
        //rather retrieve cost price from product package
        List<UUID> produtIds = dto.getItems()
                .stream()
                .map(SaleCreationDto::getProductId)
                .collect(Collectors.toList());
        
        Set<Product> products = new LinkedHashSet<>(productRepository.findAllById(produtIds));
        
        Map<UUID, Product> productMap = products
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        
        List<Sale> sales = dto.getItems()
                .stream()
                .map((s)->buildSale(s, productMap.get(s.getProductId())))
                .collect(Collectors.toList());
        
        List<Sale> savedSales = saleRepository.saveAll(sales);
        
        //create stock movement
        StockMovementInstruction smInstruction = buildStockMovementInstruction(sales, dto);
        stockMovementService.create(smInstruction);
        
        List<SaleDto> responseItems = savedSales
                .stream()
                .map(SaleDto::from)
                .collect(Collectors.toList());
        return new SalesCreationResponse(responseItems);
    }

    @Override
    public SaleDto fetch(UUID id) {
        Sale s = saleRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Product not found with id: " + id));
        return SaleDto.from(s);
    }

    @Override
    public List<SaleDto> list(SaleQueryParams params) {
        List<Sale> sales = saleRepository.list(params);
        
        return sales.stream()
                .map((p)-> SaleDto.from(p) )
                .collect(Collectors.toList());        
    }

    @Override
    public CountDto count(SaleQueryParams params) {
        return CountDto
                .builder()
                .count(saleRepository.countRecords(params))
                .build();        
    }
    
    private Sale buildSale(SaleCreationDto dto, Product p){
        ItemPrice<SaleCreationDto> itemPrice = 
                new ItemPrice<>(dto,
                        dto.getUnitPrice(),
                        dto.getDiscountRate(),
                        dto.getQuantity(),
                        dto.getTaxRate());
        
        Sale sale = Sale.builder()
                .discountRate(dto.getDiscountRate())
                .pckQty(dto.getPckQty())
                .performedBy(dto.getPerformedBy())
                .product(p)
                .quantity(dto.getQuantity())
                .sku(p.getStockKeepingUnit())
                .store(p.getStore())
                .taxRate(dto.getTaxRate())
                .transactionRef(dto.getTransactionRef())
                .unitPrice(dto.getUnitPrice())
                .preDiscountPrice(itemPrice.getPreDiscountAmount())
                .discountAmount(itemPrice.getDiscountAmount())
                .discountRate(itemPrice.getDiscountRate())
                .discountedAmount(itemPrice.getDiscountedPrice())
                .taxAmount(itemPrice.getTaxAmount())
                .taxRate(itemPrice.getTaxRate())
                .amount(itemPrice.getSum())
                .build();
        
        return sale;
    }
    
    private StockMovementInstruction buildStockMovementInstruction(List<Sale> sales, SalesCreationDto dto){
        
        
        List<StockMovementInstructionItem> instructionItems = sales
                .stream()
                .map((s)->{
                    List<PckQty> qtyList = new ArrayList<>(s.getPckQty());
                    return new StockMovementInstructionItem(s.getQuantity(), StockFlow.OUTFLOW, s.getProduct().getId(), qtyList);})
                .collect(Collectors.toList());
        
        StockMovementInstruction smInstruction = StockMovementInstruction.builder()
                .description("sales")
                .instructionItems(instructionItems)
                .performedBy(dto.getPerformedBy())
                .transactionRef(dto.getTransactionRef())                
                .build();
        
        return smInstruction;
    }
}
