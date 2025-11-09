/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.product.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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
public class ProductUpdateDto {
    @NotNull
    private String name;
    @NotNull
    private String code;
    private String category;
    private String subcategory;
    private String stockKeepingUnit;
    private Money unitPrice;
    private List<ProductPackageDto> packages;
    
    
    private String brand;
    private String description;
    private String barCode;
    private String location;
    private Double reorderLevel;
    private Double reorderQuantity;
}
