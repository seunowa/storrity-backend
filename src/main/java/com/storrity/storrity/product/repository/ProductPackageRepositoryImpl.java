/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.product.repository;

import com.storrity.storrity.product.entity.ProductPackage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Seun Owa
 */
@Repository
public class ProductPackageRepositoryImpl  implements ProductPackageRepositoryCustom{
    
    @PersistenceContext
    private EntityManager em;    
    
   @Override
    public List<String> findPackages(String query, Integer limit, UUID storeId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<ProductPackage> root = cq.from(ProductPackage.class);

        cq.select(root.get("name")).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        // Subquery for filtering by store ID
        if (storeId != null) {
            Subquery<UUID> subquery = cq.subquery(UUID.class);
            Root<com.storrity.storrity.product.entity.Product> productRoot =
                subquery.from(com.storrity.storrity.product.entity.Product.class);

            subquery.select(productRoot.get("id"))
                    .where(cb.equal(productRoot.get("store").get("id"), storeId));

            predicates.add(root.get("productId").in(subquery));
        }

        // Case-insensitive LIKE filter for query string
        if (query != null && !query.isBlank()) {
            String likePattern = "%" + query.toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get("name")), likePattern));
        }

        // Apply predicates if any exist
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(Predicate[]::new));
        }

        // Alphabetical order
        cq.orderBy(cb.asc(root.get("name")));

        TypedQuery<String> typedQuery = em.createQuery(cq);
        typedQuery.setMaxResults(limit);

        return typedQuery.getResultList();
    }
}
