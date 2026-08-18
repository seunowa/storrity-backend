/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.util.entity;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.entity.PckQtyConverter;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
 * @author Seun Owa
 */@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "supply_order_item")
public class OrderItem {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private UUID supplyId;

    private UUID productId;
    private String productName;
    private String productCode;
    private String productCategory;
    private String productSubCategory;

    private UUID storeId;
    private String storeName;

    private Double quantityOrdered;
    private String baseUnit;
    @Convert(converter = PckQtyConverter.class)
    private List<PckQty> pckQty;

    @AttributeOverrides({
        @AttributeOverride(
            name = "valueInMicroNaira",
            column = @Column(
                name = "unit_cost_price_in_micro_naira"
            )
        )
    })
    private Money costPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
