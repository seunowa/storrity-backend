/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.stockmovement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.storrity.storrity.sales.entity.PckQtyWithSellinPrice;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
//@TOdo instead of @JsonIgnoreProperties use single class (PckQty) that define all the fields (including selling price)
//in the entity and expose dto that have the required inheritance hierarchy in the api to prevent wrong use
public class PckQty {
    @NotNull
    private String packageName;
    @NotNull
    private Double quantity;  
}
