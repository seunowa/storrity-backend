/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author Seun Owa
 */
@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, Long> {

    @Override
    public Long convertToDatabaseColumn(Money money) {
        return (money != null) ? money.getValueInMicroNaira() : null;
    }

    @Override
    public Money convertToEntityAttribute(Long dbData) {
        return (dbData != null) ? new Money(dbData) : null;
    }
}