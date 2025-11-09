/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.dto;

import com.storrity.storrity.cashaccounts.entity.Money;

/**
 *
 * @author Seun Owa
 */
public class ItemPrice <T>{
    private final T id;
    private final Double discountRate;
    private final Double quantity;
    private final Double taxRate;
    private final Money unitPrice;
    private final Money preDiscountAmount;
    private final Money discountAmount;
    private final Money discountedPrice;
    private final Money taxAmount;
    private final Money sum;

    public ItemPrice( T id, Money unitPrice, Double discountRate, Double quantity, Double taxRate) {
        this.id = id;
        this.unitPrice = unitPrice;
        this.discountRate = discountRate;
        this.quantity = quantity;
        this.taxRate = taxRate;
        this.preDiscountAmount = computePreDiscountPrice();
        this.discountAmount = computeDiscountAmount();
        this.discountedPrice = computeDiscountedPrice();
        this.taxAmount = computeTaxAmount();
        this.sum = computeSum();
    }    
    
    private Money computePreDiscountPrice(){
        if (unitPrice == null || quantity == null){
            return new Money(0L);
        }
        return unitPrice.multiply(quantity);
    }
    
    private Money computeDiscountAmount(){
        if(discountRate == null || discountRate <= 0){
            return new Money(0L);
        }
        return preDiscountAmount.multiply(discountRate);        
    }
    
    private Money computeDiscountedPrice(){
        return preDiscountAmount.subtract(discountAmount);
    }
    
    private Money computeTaxAmount(){
        if(taxRate == null){
            return  new Money(0L);
        }
        return discountedPrice.multiply(taxRate);
    }
    
    private Money computeSum(){
        return discountedPrice.add(taxAmount);
    }

    public T getId() {
        return id;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public Money getPreDiscountAmount() {
        return preDiscountAmount;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public Money getDiscountedPrice() {
        return discountedPrice;
    }

    public Money getTaxAmount() {
        return taxAmount;
    }

    public Money getSum() {
        return sum;
    }
}
