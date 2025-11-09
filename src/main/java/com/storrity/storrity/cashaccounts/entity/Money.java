/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Seun Owa
 */
@Embeddable
public class Money {

    private long valueInMicroNaira; // 0.0001 NGN (4 decimal places)

    protected Money() {} // For JPA

    public Money(long valueInMicroNaira) {
        this.valueInMicroNaira = valueInMicroNaira;
    }

    public BigDecimal toNaira() {
        return BigDecimal.valueOf(valueInMicroNaira).divide(BigDecimal.valueOf(10_000));
    }

    public static Money fromNaira(BigDecimal naira) {
        return new Money(naira.multiply(BigDecimal.valueOf(10_000)).longValueExact());
    }

    @JsonValue
    public BigDecimal jsonValue() {
        return toNaira();
    }

    @JsonCreator
    public static Money fromJson(BigDecimal value) {
        return fromNaira(value);
    }

    public long getValueInMicroNaira() {
        return valueInMicroNaira;
    }

    public Money add(Money other) {
        return new Money(this.valueInMicroNaira + other.valueInMicroNaira);
    }

    public Money subtract(Money other) {
        return new Money(this.valueInMicroNaira - other.valueInMicroNaira);
    }
    
    public Money multiply(Double multiplier) {
        long result = Math.round(this.valueInMicroNaira * multiplier);
        return new Money(result);
    }
    
    public Money multiply(BigDecimal multiplier) {
        BigDecimal result = BigDecimal.valueOf(valueInMicroNaira).multiply(multiplier);
        return new Money(result.longValue());
    }

    public boolean isPositiveValue() {
        return getValueInMicroNaira() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return valueInMicroNaira == money.valueInMicroNaira;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueInMicroNaira);
    }
    
    @Override
    public String toString() {
        return toNaira().toPlainString() + " NGN";
    }
    
    public int compareTo(Money other) {
        return Long.compare(this.valueInMicroNaira, other.valueInMicroNaira);
    }
}
