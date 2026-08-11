/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.ai.settings.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author Seun Owa
 */
@Converter
public class OperatingHoursConverter implements AttributeConverter<OperatingHours, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules(); // registers JavaTimeModule for LocalTime

    @Override
    public String convertToDatabaseColumn(OperatingHours attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize OperatingHours", e);
        }
    }

    @Override
    public OperatingHours convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            return MAPPER.readValue(dbData, OperatingHours.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize OperatingHours", e);
        }
    }
}