/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.entity;

import java.util.List;
/**
 *
 * @author Seun Owa
 */
public class SupplyProcessTemplate {
    
     public List<SupplyAction> getComprehensive() {
        return List.of(SupplyAction.DRAFT,
            SupplyAction.SUBMIT_DRAFT,
            SupplyAction.APPROVE_DRAFT,
            SupplyAction.ORDER,
            SupplyAction.DELIVER,
            SupplyAction.SUBMIT_DELIVERY,
            SupplyAction.APPROVE_DELIVERY,
            SupplyAction.RECEIVE
        );
    }

    public List<SupplyAction> getStandard() {
        return List.of(
            SupplyAction.DRAFT,
            SupplyAction.ORDER,
            SupplyAction.DELIVER,
            SupplyAction.RECEIVE
        );
    }

    public List<SupplyAction> getSimple() {
        return List.of(
            SupplyAction.DRAFT,
            SupplyAction.DELIVER,
            SupplyAction.RECEIVE
        );
    }

    public List<SupplyAction> getMinimal() {
        return List.of(
            SupplyAction.DRAFT,
            SupplyAction.RECEIVE
        );
    }
}
