/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.util.sort;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Seun Owa
 */
public class SortUtils {
    public static <T> List<Order> buildDynamicOrder(
            CriteriaBuilder cb,
            Root<T> root,
            String sortParam // raw sort string, e.g., "name:asc,createdAt:desc"
    ) {
        List<SortProperty> sortProperties = SortPropertyParser.parse(sortParam);
        return sortProperties.stream()
                .map(sp -> sp.getSortDirection() == SortDirection.ASC
                        ? cb.asc(root.get(sp.getPropertyName()))
                        : cb.desc(root.get(sp.getPropertyName()))
                )
                .collect(Collectors.toList());
    }
}
