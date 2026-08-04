/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AverageBasketDto {

    /**
     * Number of completed baskets (transactionRef count)
     */
    private Long totalBaskets;

    /**
     * Sum of all revenue across all baskets
     */
    private Money totalRevenue;

    /**
     * Average basket value (ABV)
     */
    private Money averageBasketValue;

    /**
     * Median basket value.
     * Better representation than average because
     * it is less affected by unusually large baskets.
     */
    private Money medianBasketValue;

    /**
     * Smallest basket sold.
     */
    private Money smallestBasketValue;

    /**
     * Largest basket sold.
     */
    private Money largestBasketValue;

    /**
     * Average value before discounts.
     */
    private Money averageGrossBasketValue;

    /**
     * Average discount applied.
     */
    private Money averageDiscountPerBasket;

    /**
     * Average tax collected.
     */
    private Money averageTaxPerBasket;

    /**
     * Average quantity of items purchased.
     */
    private Double averageItemsPerBasket;

    /**
     * Average number of unique products.
     */
    private Double averageUniqueProductsPerBasket;

    /**
     * Average quantity per unique product.
     * Indicates whether customers buy multiples
     * of the same SKU.
     */
    private Double averageUnitsPerProduct;

    /**
     * Total quantity sold.
     */
    private Double totalItemsSold;

    /**
     * Number of baskets with at least one discount.
     */
    private Long discountedBasketCount;

    /**
     * Percentage of baskets that received discounts.
     */
    private Double discountedBasketPercentage;

    /**
     * Average discount percentage.
     */
    private Double averageDiscountRate;
}
