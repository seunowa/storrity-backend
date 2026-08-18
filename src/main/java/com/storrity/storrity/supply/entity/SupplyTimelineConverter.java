/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.supply.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
@Converter
public class SupplyTimelineConverter
        implements AttributeConverter<List<SupplyTimelineEntry>, String> {

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static final TypeReference<List<SupplyTimelineEntry>> TYPE =
            new TypeReference<>() {};
    
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Override
    public String convertToDatabaseColumn(
            List<SupplyTimelineEntry> actions) {

        if (actions == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(actions);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    "Unable to serialize supply timeline",
                    e
            );
        }
    }

    @Override
    public List<SupplyTimelineEntry> convertToEntityAttribute(
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
                    "Unable to deserialize supply timeline",
                    e
            );
        }
    }
}
