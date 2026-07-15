/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.sales.entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public class PckQtyWithSellingPriceConverter implements AttributeConverter<List<PckQtyWithSellinPrice>, String>{
    private final ObjectMapper objectMapper;

    public PckQtyWithSellingPriceConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String convertToDatabaseColumn(List<PckQtyWithSellinPrice> attribute) {

        if (attribute == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error serializing package quantities.", ex);
        }
        
        //@TOdo check other converters in the code refactoe the code to be like this
    }

    @Override
    public List<PckQtyWithSellinPrice> convertToEntityAttribute(String dbData) {
        
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(
                    dbData,
                    new TypeReference<List<PckQtyWithSellinPrice>>() {});
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error deserializing package quantities.", ex);
        }
    }
}
