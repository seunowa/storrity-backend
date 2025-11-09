/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.dto;

import com.storrity.storrity.cashaccounts.entity.Money;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Seun Owa
 * @param <T> ID type
 */
public class PriceHelper <T>{
        
    private final List<ItemPrice<T>> items;
    private final Map<T, ItemPrice<T>> itemPriceMap;
    private final Money totalPreDiscountAmount;
    private final Money totalDiscountAmount;
    private final Money totalDiscountedPrice;
    private final Money totalTaxAmount;
    private final Money grandTotal;

    public PriceHelper(List<ItemPrice<T>> items) {
        this.items = items == null ? List.of() : items;
        this.itemPriceMap = this.items.stream()
        .collect(Collectors.toMap(ItemPrice::getId, Function.identity(), (a, b) -> a));
        this.totalPreDiscountAmount = computeTotal(ItemPrice::getPreDiscountAmount);
        this.totalDiscountAmount = computeTotal(ItemPrice::getDiscountAmount);
        this.totalDiscountedPrice = computeTotal(ItemPrice::getDiscountedPrice);
        this.totalTaxAmount = computeTotal(ItemPrice::getTaxAmount);
        this.grandTotal = computeTotal(ItemPrice::getSum);
    }  
    
    private Money computeTotal(Function<ItemPrice<T>, Money> extractor) {
        return items.stream()
                .map(extractor)
                .reduce(new Money(0L), Money::add);
    }

    public ItemPrice<T> getItem(T id) {
        return itemPriceMap.get(id);
    }

    public Money getTotalPreDiscountAmount() {
        return totalPreDiscountAmount;
    }

    public Money getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public Money getTotalDiscountedPrice() {
        return totalDiscountedPrice;
    }

    public Money getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public Money getGrandTotal() {
        return grandTotal;
    }
}
