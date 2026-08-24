/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stocktransfer.entity;

import java.util.List;

/**
 *
 * @author Seun Owa
 */
public class StockTransferProcessTemplate {

    public List<StockTransferAction> getComprehensive() {

        return List.of(
            StockTransferAction.DRAFT,
            StockTransferAction.SUBMIT_DRAFT,
            StockTransferAction.APPROVE_DRAFT,
            StockTransferAction.SEND,
            StockTransferAction.SUBMIT_RECEIPT,
            StockTransferAction.APPROVE_RECEIPT,
            StockTransferAction.RECEIVE
        );
    }

    public List<StockTransferAction> getStandard() {

        return List.of(
            StockTransferAction.DRAFT,
            StockTransferAction.SEND,
            StockTransferAction.RECEIVE
        );
    }

    public List<StockTransferAction> getSimple() {

        return List.of(
            StockTransferAction.DRAFT,
            StockTransferAction.SEND,
            StockTransferAction.RECEIVE
        );
    }

    public List<StockTransferAction> getMinimal() {

        return List.of(
            StockTransferAction.DRAFT,
            StockTransferAction.RECEIVE
        );
    }
}