/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.storrity.storrity.stocktransfer.entity;

/**
 *
 * @author Seun Owa
 */
public enum StockTransferStatus {

    DRAFT,
    AWAITING_DRAFT_APPROVAL,
    DRAFT_APPROVED,
    SENT,
    AWAITING_RECEIPT_APPROVAL,
    RECEIPT_APPROVED,
    RECEIVED,
    CANCELED
}
