/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.product.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.store.entity.Store;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

/**
 *
 * @author Seun Owa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product_defination")
public class ProductDefination {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "code")
    private String code;    
    @Column(name = "category")
    private String category;
    @Column(name = "subcategory")
    private String subcategory;
    @NotNull
    @Column(name = "stock_keeping_unit")
    private String stockKeepingUnit;
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "unit_price_in_micro_naira"))
    })
    private Money unitPrice;
    @OneToMany(mappedBy = "productId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<ProductPackage> packages;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType productType;
    private String brand;
    private String description;
    private String barCode;
    private String location;
    private Double minimumStockLevel;
    private Double reorderLevel;
    private Double reorderQuantity;
    private Double maximumStockLevel;
}
