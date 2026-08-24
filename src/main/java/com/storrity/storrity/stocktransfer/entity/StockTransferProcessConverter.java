/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.stocktransfer.entity;

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
public class StockTransferProcessConverter 
        implements AttributeConverter<List<StockTransferAction>, String> {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper();

    private static final TypeReference<List<StockTransferAction>> TYPE =
            new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(
            List<StockTransferAction> actions) {

        if (actions == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(actions);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                "Unable to serialize stock transfer process",
                e
            );
        }
    }

    @Override
    public List<StockTransferAction> convertToEntityAttribute(
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
                "Unable to deserialize stock transfer process",
                e
            );
        }
    }
}
