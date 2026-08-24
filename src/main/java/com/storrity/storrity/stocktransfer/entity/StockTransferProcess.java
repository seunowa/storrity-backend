/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stocktransfer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Seun Owa
 */

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransferProcess {

    @Convert(converter = StockTransferProcessConverter.class)
    @Column(
        name = "stock_transfer_process",
        columnDefinition = "TEXT"
    )
    private List<StockTransferAction> actions;

    public void validate() {

        if (actions == null || actions.isEmpty()) {
            throw new IllegalArgumentException(
                "Stock transfer process cannot be empty"
            );
        }

        if (actions.get(0) != StockTransferAction.DRAFT) {
            throw new IllegalArgumentException(
                "Stock transfer process must start with DRAFT"
            );
        }

        if (actions.get(actions.size() - 1)
                != StockTransferAction.RECEIVE) {

            throw new IllegalArgumentException(
                "Stock transfer process must end with RECEIVE"
            );
        }

        if (actions.contains(StockTransferAction.CANCEL)) {
            throw new IllegalArgumentException(
                "CANCEL cannot be part of the normal stock transfer process"
            );
        }

        Set<StockTransferAction> seen =
            EnumSet.noneOf(StockTransferAction.class);

        int previousOrder = -1;

        for (StockTransferAction action : actions) {

            if (!seen.add(action)) {
                throw new IllegalArgumentException(
                    "Duplicate action: " + action
                );
            }

            int order = action.ordinal();

            if (order <= previousOrder) {
                throw new IllegalArgumentException(
                    "Invalid action order: " + action
                );
            }

            previousOrder = order;
        }

        if (seen.contains(StockTransferAction.APPROVE_DRAFT)
                && !seen.contains(StockTransferAction.SUBMIT_DRAFT)) {

            throw new IllegalArgumentException(
                "To have " + StockTransferAction.APPROVE_DRAFT
                + ", " + StockTransferAction.SUBMIT_DRAFT
                + " is required"
            );
        }

        if (seen.contains(StockTransferAction.APPROVE_RECEIPT)
                && !seen.contains(StockTransferAction.SUBMIT_RECEIPT)) {

            throw new IllegalArgumentException(
                "To have " + StockTransferAction.APPROVE_RECEIPT
                + ", " + StockTransferAction.SUBMIT_RECEIPT
                + " is required"
            );
        }
    }
}