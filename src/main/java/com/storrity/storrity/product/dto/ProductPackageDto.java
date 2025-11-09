/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.product.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.ProductPackage;
import jakarta.validation.constraints.NotNull;
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
public class ProductPackageDto {
    @NotNull
    private String name;
    @NotNull
    private Double multiplier;
    @NotNull
    private Money sellingPrice;
    
    public static ProductPackageDto from(ProductPackage pk){
        ProductPackageDto dto = new ProductPackageDto();
        dto.setMultiplier(pk.getMultiplier());
        dto.setName(pk.getName());
        dto.setSellingPrice(pk.getSellingPrice());
        return dto;
    }
}
