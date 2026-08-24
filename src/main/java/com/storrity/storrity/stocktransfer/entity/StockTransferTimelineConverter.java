/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stocktransfer.entity;

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
public class StockTransferTimelineConverter
        implements AttributeConverter<
            List<StockTransferTimelineEntry>, String> {

    private static final ObjectMapper OBJECT_MAPPER =
            createObjectMapper();

    private static final TypeReference<
            List<StockTransferTimelineEntry>> TYPE =
            new TypeReference<>() {};

    private static ObjectMapper createObjectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    @Override
    public String convertToDatabaseColumn(
            List<StockTransferTimelineEntry> entries) {

        if (entries == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(entries);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                "Unable to serialize stock transfer timeline",
                e
            );
        }
    }

    @Override
    public List<StockTransferTimelineEntry>
            convertToEntityAttribute(String value) {

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
                "Unable to deserialize stock transfer timeline",
                e
            );
        }
    }
}