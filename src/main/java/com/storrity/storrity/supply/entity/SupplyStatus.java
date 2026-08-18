/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.storrity.storrity.supply.entity;

/**
 *
 * @author Seun Owa
 */
public enum SupplyStatus {
    DRAFT,
    AWAITING_ORDER_APPROVAL,
    DRAFT_APPROVED,
    ORDERED,//The store has committed to obtaining the goods from the supplier.
    DELIVERED,//supplier delivers goods to store for inspection + verification + approval
    AWAITING_DELIVERY_APPROVAL,
    DELIVERY_APPROVED,
    RECEIVED,//The store has approved/accepted the delivered goods and they are now recognized as inventory.
    CANCELED
}
