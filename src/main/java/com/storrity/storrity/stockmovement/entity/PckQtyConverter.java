/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.stockmovement.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public class PckQtyConverter implements AttributeConverter<List<PckQty>, String>{
    private final ObjectMapper objectMapper;

    public PckQtyConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String convertToDatabaseColumn(List<PckQty> attribute) {
        //Serialize list to json string
        JsonNode jsonNode  = objectMapper.valueToTree(attribute);
        return jsonNode.toString();
    }

    @Override
    public List<PckQty> convertToEntityAttribute(String dbData) {
        try {
            //deserialize json string to list
            JsonNode jsonNode = objectMapper.readTree(dbData);
            return objectMapper.treeToValue(jsonNode, List.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error deserializing metadata");
        }
    }
}
