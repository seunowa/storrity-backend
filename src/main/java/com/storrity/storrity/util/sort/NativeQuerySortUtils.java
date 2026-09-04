/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.sort;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds ORDER BY clauses for native/raw SQL queries from a sort phrase
 * (e.g. "netSales:desc,storeName:asc").
 *
 * Unlike {@link SortUtils}, which relies on JPA's CriteriaBuilder/Root to
 * safely resolve property names (and therefore can accept any entity
 * attribute), native queries have no such safety net - a property name
 * taken from user input and concatenated directly into SQL is a SQL
 * injection risk. So callers must supply an explicit allow-list mapping
 * DTO/API property names to the actual column (or column alias) each one
 * is allowed to sort by. Any requested sort property not present in the
 * allow-list is silently ignored rather than rejected outright, so an
 * unrecognized field in a multi-field sort doesn't blow up the whole
 * request.
 *
 * @author Seun Owa
 */
public class NativeQuerySortUtils {

    /**
     * @param sortParam            raw sort string, e.g. "netSales:desc,storeName:asc"
     * @param allowedSortProperties map of API/DTO property name -> SQL column/alias
     *                               it is allowed to sort by
     * @param defaultOrderByClause  full clause (including the leading " ORDER BY ...")
     *                               to fall back to when no requested property is valid
     * @return a full " ORDER BY ..." clause ready to be appended to the query
     */
    public static String buildOrderByClause(
            String sortParam,
            Map<String, String> allowedSortProperties,
            String defaultOrderByClause) {

        List<SortProperty> sortProperties = SortPropertyParser.parse(sortParam);

        List<SortProperty> validSortProperties = sortProperties.stream()
                .filter(sp -> allowedSortProperties.containsKey(sp.getPropertyName()))
                .collect(Collectors.toList());

        if (validSortProperties.isEmpty()) {
            return defaultOrderByClause;
        }

        String orderBy = validSortProperties.stream()
                .map(sp -> allowedSortProperties.get(sp.getPropertyName())
                        + " "
                        + sp.getSortDirection().name())
                .collect(Collectors.joining(", "));

        return " ORDER BY " + orderBy;
    }
}
