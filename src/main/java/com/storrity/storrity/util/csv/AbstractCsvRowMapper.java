/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.util.csv;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.util.exception.InputValidationAppException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public abstract class AbstractCsvRowMapper<I, E> {
    
    public Map<String, Integer> indexHeader(String[] header) {
        Map<String, Integer> index = new LinkedHashMap<>();

        for (int i = 0; i < header.length; i++) {
            index.put(header[i].trim().toLowerCase(), i);
        }

        for (String required : headers()) {
            if (!index.containsKey(required.toLowerCase())) {
                throw new InputValidationAppException(
                        "CSV is missing required column: " + required);
            }
        }

        return index;
    }

    protected abstract String[] headers();

    public abstract I fromCsvRow(Map<String,Integer> headerIndex, String[] row, CsvContext context);

    public abstract String[] toCsvRow(E entity);    
    
    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------
    
    protected String field(String[] row, Map<String, Integer> headerIndex, String name) {
        Integer idx = headerIndex.get(name.toLowerCase());
        if (idx == null || idx >= row.length) {
            return "";
        }
        String v = row[idx];
        return v == null ? "" : v.trim();
    }

    protected String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    protected String nullToBlank(String s) {
        return s == null ? "" : s;
    }

    protected Double parseDouble(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            return Double.valueOf(s.trim());
        } catch (NumberFormatException e) {
            throw new InputValidationAppException("Invalid number: '" + s + "'");
        }
    }

    protected String formatDouble(Double d) {
        if (d == null) {
            return "";
        }
        // avoid "12.0" noise for whole numbers, e.g. multipliers
        if (d == Math.floor(d) && !d.isInfinite()) {
            return String.valueOf(d.longValue());
        }
        return String.valueOf(d);
    }

    protected Money csvToMoney(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            return Money.fromNaira(new BigDecimal(s.trim()));
        } catch (ArithmeticException | NumberFormatException e) {
            throw new InputValidationAppException("Invalid money value: '" + s + "'");
        }
    }

    protected String moneyToCsv(Money money) {
        if (money == null) {
            return "";
        }
        return money.toNaira().stripTrailingZeros().toPlainString();
    }
    
    protected static UUID parseUuid(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(s.trim());
        } catch (IllegalArgumentException e) {
            throw new InputValidationAppException("Invalid UUID: '" + s + "'");
        }
    }
}
