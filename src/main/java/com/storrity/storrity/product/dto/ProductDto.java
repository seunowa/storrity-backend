/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.product.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Schema(description = "Product response object")
public class ProductDto {
    private UUID id;
    private String name;
    private String code;  
    private String category;
    private String subcategory;
    private String stockKeepingUnit;
    private Money unitPrice;
    private Double qtyInStock;
    private String storeName;
    private UUID storeId;
    private Collection<ProductPackageDto> packages;
    private LocalDateTime lastMovementAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static ProductDto from(Product p) {
        ProductDto dto = new ProductDto();
        dto.setCategory(p.getCategory());
        dto.setCode(p.getCode());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setPackages(p.getPackages().stream()
                .map((pk)->ProductPackageDto.from(pk))
                .collect(Collectors.toList()));
        dto.setQtyInStock(p.getQtyInStock());
        dto.setStockKeepingUnit(p.getStockKeepingUnit());
        dto.setStoreId(p.getStore().getId());
        dto.setStoreName(p.getStore().getName());
        dto.setSubcategory(p.getSubcategory());
        dto.setUnitPrice(p.getUnitPrice());
        dto.setUpdatedAt(p.getUpdatedAt());
        dto.setLastMovementAt(p.getLastMovementAt());
        
        return dto;
    }
}
