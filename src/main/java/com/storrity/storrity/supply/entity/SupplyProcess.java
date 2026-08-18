/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.entity;

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
public class SupplyProcess {

    @Convert(converter = SupplyProcessConverter.class)
    @Column(
        name = "supply_process",
        columnDefinition = "TEXT"
    )
    private List<SupplyAction> actions;

    public void validate() {

        if (actions == null || actions.isEmpty()) {
            throw new IllegalArgumentException(
                "Supply process cannot be empty"
            );
        }

        if (actions.get(0) != SupplyAction.DRAFT) {
            throw new IllegalArgumentException(
                "Supply process must start with DRAFT"
            );
        }

        if (actions.get(actions.size() - 1)
                != SupplyAction.RECEIVE) {

            throw new IllegalArgumentException(
                "Supply process must end with RECEIVE"
            );
        }
        
        if (actions.contains(SupplyAction.CANCEL)) {
            throw new IllegalArgumentException(
                "CANCEL cannot be part of the normal supply process"
            );
        }

        Set<SupplyAction> seen =
            EnumSet.noneOf(SupplyAction.class);

        int previousOrder = -1;

        for (SupplyAction action : actions) {

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
        
        if(seen.contains(SupplyAction.APPROVE_DRAFT) && !seen.contains(SupplyAction.SUBMIT_DRAFT)){
            throw new IllegalArgumentException("To have " + SupplyAction.APPROVE_DRAFT + " " + SupplyAction.SUBMIT_DRAFT + "is required");
        }
        
        if(seen.contains(SupplyAction.APPROVE_DELIVERY) && !seen.contains(SupplyAction.SUBMIT_DELIVERY)){
            throw new IllegalArgumentException("To have " + SupplyAction.APPROVE_DELIVERY + " " + SupplyAction.SUBMIT_DELIVERY + "is required");
        }
    }
}
