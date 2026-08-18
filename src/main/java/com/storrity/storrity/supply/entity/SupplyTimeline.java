/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.entity;

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
public class SupplyTimeline {
    
    @Convert(converter = SupplyTimelineConverter.class)
    @Column(
        name = "supply_timeline",
        columnDefinition = "TEXT"
    )
    private List<SupplyTimelineEntry> items;
    
    public List<SupplyTimelineEntry> appendEntry(SupplyTimelineEntry entry){
        if(items == null){
            items = new ArrayList();
        }
        items.add(entry);
        return items;
    }
}
