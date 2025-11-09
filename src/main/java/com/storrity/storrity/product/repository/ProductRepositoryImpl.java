/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.product.repository;

import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductQueryParams;
import com.storrity.storrity.util.sort.SortUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Seun Owa
 */
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> list(ProductQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = cb.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        
        List<Predicate> predicates = buildPredicate(cb, root, params);
        
        // Combine all predicates using AND
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(Predicate[]::new));
        } 
        
        if(params.getSort() != null){           
            List<Order> orders = SortUtils.buildDynamicOrder(cb, root, params.getSort());
            criteriaQuery.orderBy(orders);
        }
        
        TypedQuery<Product> tq = em.createQuery(criteriaQuery);
        
        //if offest is provided apply offset
        if( null != params.getOffset()){
            tq.setFirstResult(params.getOffset());
        }
        
        //if limit is provided apply limit
        if( null != params.getLimit()){
            tq.setMaxResults(params.getLimit());
        }
        
        return tq.getResultList();
    }

    @Override
    public Long countRecords(ProductQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> root = cq.from(Product.class);        
        
        //build predicates
        List<Predicate> predicates = buildPredicate(cb, root, params);        
        
        //execute query        
        cq.select(cb.count(root));
        cq.where(predicates.toArray(Predicate[]::new));
        
        return em.createQuery(cq).getSingleResult();
    }

    private List<Predicate> buildPredicate(CriteriaBuilder cb, Root<Product> root, ProductQueryParams params) {
        List<Predicate> predicates = new ArrayList<>();        
                
        if (params.getIds()!= null && !params.getIds().isEmpty()) {
            predicates.add(root.get("id").in(params.getIds()));
        }      
                
        if (params.getNames()!= null && !params.getNames().isEmpty()) {
            predicates.add(root.get("name").in(params.getNames()));
        }      
                
        if (params.getCodes()!= null && !params.getCodes().isEmpty()) {
            predicates.add(root.get("code").in(params.getCodes()));
        }      
                
        if (params.getCategory()!= null && !params.getCategory().isEmpty()) {
            predicates.add(root.get("category").in(params.getCategory()));
        }      
                
        if (params.getSubcategory()!= null && !params.getSubcategory().isEmpty()) {
            predicates.add(root.get("subcategory").in(params.getSubcategory()));
        }      
                
        if (params.getStockKeepingUnit()!= null && !params.getStockKeepingUnit().isEmpty()) {
            predicates.add(root.get("stockKeepingUnit").in(params.getStockKeepingUnit()));
        }      
                
        if (params.getStoreIds()!= null && !params.getStoreIds().isEmpty()) {
            predicates.add(root.get("store").get("id").in(params.getStoreIds()));
        }

        // Conditionally add date range filter
        if (params.getCreatedAtRange()!= null && params.getCreatedAtRange().size() == 2) {
            predicates.add(cb.between(root.get("createdAt")
                    , params.getCreatedAtRange().get(0)
                    , params.getCreatedAtRange().get(1)));
        }

        // Conditionally add date range filter
        if (params.getUpdatedAtRange()!= null && params.getUpdatedAtRange().size() == 2) {
            predicates.add(cb.between(root.get("updatedAt")
                    , params.getUpdatedAtRange().get(0)
                    , params.getUpdatedAtRange().get(1)));
        }
        
        if (params.getSearchPhrase() != null && !params.getSearchPhrase().isBlank()) {
            String likePattern = "%" + params.getSearchPhrase().toLowerCase() + "%";
            Predicate nameLike = cb.like(cb.lower(root.get("name")), likePattern);
            Predicate codeLike = cb.like(cb.lower(root.get("code")), likePattern);
            predicates.add(cb.or(nameLike, codeLike));
        }
        
        return predicates;
    }
    
    
    @Override
    public List<String> listProductNames(String query, Integer limit, UUID storeId) {
        return findDistinctValues("name", query, limit, storeId);
    }
    
    
    @Override
    public List<String> listCategories(String query, Integer limit, UUID storeId) {
        return findDistinctValues("category", query, limit, storeId);
    }

    @Override
    public List<String> listSubcategories(String query, Integer limit, UUID storeId) {
        return findDistinctValues("subcategory", query, limit, storeId);
    }

    @Override
    public List<String> listBrands(String query, Integer limit, UUID storeId) {
        return findDistinctValues("brand", query, limit, storeId);
    }

    @Override
    public List<String> listStockKeepingUnits(String query, Integer limit, UUID storeId) {
        return findDistinctValues("stockKeepingUnit", query, limit, storeId);
    }

    /**
     * Utility method for building a distinct, alphabetical, case-insensitive list
     * of values from a specific field.
     */
    private List<String> findDistinctValues(String field, String query, int limit, UUID storeId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Product> root = cq.from(Product.class);

        // select DISTINCT field
        cq.select(root.get(field)).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        // Optional store filter
        if (storeId != null) {
            predicates.add(cb.equal(root.get("store").get("id"), storeId));
        }

        // Query string filter (case-insensitive LIKE)
        if (query != null && !query.isBlank()) {
            String likePattern = "%" + query.toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get(field)), likePattern));
        }

        // Apply predicates
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(Predicate[]::new));
        }

        // Order alphabetically
        cq.orderBy(cb.asc(root.get(field)));

        // Execute query with limit
        TypedQuery<String> tq = em.createQuery(cq);
        tq.setMaxResults(limit);

        return tq.getResultList();
    }

}
