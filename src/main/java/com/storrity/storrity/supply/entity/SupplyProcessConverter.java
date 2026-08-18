/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
@Converter
public class SupplyProcessConverter
        implements AttributeConverter<List<SupplyAction>, String> {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper();

    private static final TypeReference<List<SupplyAction>> TYPE =
            new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(
            List<SupplyAction> actions) {

        if (actions == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(actions);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    "Unable to serialize supply process",
                    e
            );
        }
    }

    @Override
    public List<SupplyAction> convertToEntityAttribute(
            String value) {

        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }

        try {
            return OBJECT_MAPPER.readValue(
                    value,
                    TYPE
            );

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    "Unable to deserialize supply process",
                    e
            );
        }
    }
}
