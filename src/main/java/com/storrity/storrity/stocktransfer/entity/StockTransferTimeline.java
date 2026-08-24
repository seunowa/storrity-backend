/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stocktransfer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;
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
public class StockTransferTimeline {

    @Convert(converter = StockTransferTimelineConverter.class)
    @Column(
        name = "stock_transfer_timeline",
        columnDefinition = "TEXT"
    )
    private List<StockTransferTimelineEntry> items;

    public List<StockTransferTimelineEntry> appendEntry(
            StockTransferTimelineEntry entry) {

        if (items == null) {
            items = new ArrayList<>();
        }

        items.add(entry);

        return items;
    }
}