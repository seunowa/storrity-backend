/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.service;

import com.storrity.storrity.product.dto.StockFlow;
import com.storrity.storrity.sales.dto.SalesReturnCreationDto;
import com.storrity.storrity.sales.dto.SalesReturnDto;
import com.storrity.storrity.sales.entity.Sale;
import com.storrity.storrity.sales.entity.SalesReturn;
import com.storrity.storrity.sales.entity.SalesReturnQueryParams;
import com.storrity.storrity.sales.repository.SaleRepository;
import com.storrity.storrity.sales.repository.SalesReturnRepository;
import com.storrity.storrity.stockmovement.dto.StockMovementInstruction;
import com.storrity.storrity.stockmovement.dto.StockMovementInstructionItem;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.service.StockMovementService;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class SalesReturnServiceImpl implements SalesReturnService{
    
    private final SaleRepository saleRepository;
    private final SalesReturnRepository salesReturnRepository;
    private final StockMovementService stockMovementService;
    
    @Autowired
    public SalesReturnServiceImpl(
            SaleRepository saleRepository,
            SalesReturnRepository salesReturnRepository,
            StockMovementService stockMovementService) {

        this.saleRepository = saleRepository;
        this.salesReturnRepository = salesReturnRepository;
        this.stockMovementService = stockMovementService;
    }

    @Override
    public SalesReturnDto create(SalesReturnCreationDto dto) {

        Sale sale = saleRepository.findById(dto.getSaleId())
                .orElseThrow(() ->
                        new ResourceNotFoundAppException(
                                "Sale not found with id " + dto.getSaleId()));

        validateReturnQuantity(sale, dto);
        
//        @TOdo use security principal as performed by
        String permedBy = "";

        SalesReturn salesReturn = SalesReturn.builder()
                .transactionRef(dto.getTransactionRef())
                .sale(sale)
                .quantity(dto.getQuantity())
                .sku(sale.getSku())
                .performedBy(permedBy)
                .reason(dto.getReason())
                .pckQty(dto.getPckQty())
                .build();

        SalesReturn saved = salesReturnRepository.save(salesReturn);

        StockMovementInstruction instruction =
                buildStockMovementInstruction(saved);

        stockMovementService.create(instruction);

        return SalesReturnDto.from(saved);
    }

    @Override
    public SalesReturnDto fetch(UUID id) {
        SalesReturn s = salesReturnRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Sales retun not found with id: " + id));
        return SalesReturnDto.from(s);
    }

    @Override
    public List<SalesReturnDto> list(SalesReturnQueryParams params) {
        List<SalesReturn> salesReturn = salesReturnRepository.list(params);
        
        return salesReturn.stream()
                .map((p)-> SalesReturnDto.from(p) )
                .collect(Collectors.toList());
    }

    @Override
    public CountDto count(SalesReturnQueryParams params) {
        return CountDto
                .builder()
                .count(salesReturnRepository.countRecords(params))
                .build();
    }
    
    private void validateReturnQuantity(
        Sale sale,
        SalesReturnCreationDto dto) {

        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Return quantity must be greater than zero.");
        }

        if (dto.getQuantity() > sale.getQuantity()) {
            throw new IllegalArgumentException(
                    "Returned quantity exceeds quantity sold.");
        }
    }
    
    private StockMovementInstruction buildStockMovementInstruction(SalesReturn salesReturn) {

        Sale s = salesReturn.getSale();
        List<PckQty> qty = new ArrayList<>(salesReturn.getPckQty());
    
        StockMovementInstructionItem item = StockMovementInstructionItem
                            .builder()
                            .flow(StockFlow.INFLOW)
                            .pckQty(qty)
                            .productCategory(s.getProductCategory())
                            .productCode(s.getProductCode())
                            .productId(s.getProductId())
                            .productName(s.getProductName())
                            .productSubCategory(s.getProductSubCategory())
                            .storeId(s.getStoreId())
                            .storeName(s.getStoreName())
                            .performedBy(s.getPerformedBy())
                            .build();

        return StockMovementInstruction.builder()
                .description("sales return")
                .performedBy(salesReturn.getPerformedBy())
                .transactionRef(salesReturn.getTransactionRef())
                .instructionItems(List.of(item))
                .build();
    }
}
