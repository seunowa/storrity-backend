/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.store.entity.Store;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

/**
 *
 * @author Seun owa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String transactionRef;
//    @Todo remove relationship to product and keep only productId as property in this entity
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
    private UUID productId;
    private String productName;
    private String productCode;
    private String productCategory;
    private String productSubCategory;
//    @Todo remove relationship to store and keep only storeId as property in this entity
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "store_id", nullable = false)
//    private Store store;
    private UUID storeId;
    private String storeName;
    private String performedBy;
    private UUID customerId;
    private String customerName;
//    Client system name at the time of sale
    private String clientSystemId;
    private String clientSystemName;
    private Double quantity;
    private String sku;
    @Convert(converter = PckQtyWithSellingPriceConverter.class)
    private List<PckQtyWithSellinPrice> pckQty;
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "unit_price_in_micro_naira"))
    })
    private Money unitPrice;
    private Double discountRate;
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "pre_discount_price_in_micro_naira"))
    })
//    @Todo refactor preDiscountPrice to nonDiscountedPrice
    private Money preDiscountPrice;
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "discount_amount_in_micro_naira"))
    })
    private Money discountAmount;
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "discounted_amount_in_micro_naira"))
    })
//    @Todo refactor discountedAmount to discountedPrice
    private Money discountedAmount;
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "amount_in_micro_naira"))
    })
    private Money amount;
    private Double taxRate;
    @AttributeOverrides({
        @AttributeOverride(name = "valueInMicroNaira", column = @Column(name = "tax_amount_in_micro_naira"))
    })
    private Money taxAmount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; 
       
    @PrePersist
    public void prePersist(){        
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }
    
    @PreUpdate
    public void preUpdate(){
        LocalDateTime now = LocalDateTime.now();
        updatedAt = now;
    }
}
