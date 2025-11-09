/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Seun Owa
 */
public class SortPropertyParser {
    public static List<SortProperty> parse(String sortPhrase){
        
        if (sortPhrase != null) {
            List<String> properyDirectionPair = Arrays.asList(sortPhrase.split(","));//parse string into a list of propery name and sort direction pair
            return properyDirectionPair.stream()
                    .map((pair)-> pair.split(":"))//split pair into and arrray of propery name and sort direction
                    .filter(pair -> pair.length == 2)//filter out pairs that dont have exactly two items
                    .map(pair -> new SortProperty(pair[0], SortDirection.valueOf(pair[1].toUpperCase())))//create sorte 
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
}
