/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.util.Map;

/**
 *
 * @author Seun Owa
 */
public class MetadataConverter implements AttributeConverter<Map<String,Object>, String>{
    private final ObjectMapper objectMapper;

    public MetadataConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> map) {
        //Serialize map to json string
        JsonNode jsonNode  = objectMapper.valueToTree(map);
        return jsonNode.toString();
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String jsonString) {
        try {
            //deserialize json string to map
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return objectMapper.treeToValue(jsonNode, Map.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error deserializing metadata");
        }
    }
    
}
