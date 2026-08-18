/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.storrity.storrity.stockmovement.entity;

/**
 *
 * @author Seun Owa
 */
public enum StockMovementType {

    OPENING_BALANCE,
    SUPPLY,//@Todo contemplating renaming this PURCHASE
    SALE,
    SALE_RETURN,
    SUPPLIER_RETURN,
    STOCK_ADJUSTMENT,
    STOCK_TRANSFER,
    PRODUCTION_CONSUMPTION,
    PRODUCTION_OUTPUT,
    PRODUCTION_LOSS,
//    I commented the lines below becase they are explanation of losses and should not be a movement type
//    They better function as sub category of stock adjustment but i dont want to add more complexity
//    DAMAGE,
//    EXPIRY,
//    LOSS,
//    DATA_CORRECTION,
    OTHER
}
