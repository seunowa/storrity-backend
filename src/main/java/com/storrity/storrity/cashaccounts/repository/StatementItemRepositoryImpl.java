/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.repository;

import com.storrity.storrity.cashaccounts.entity.StatementItem;
import com.storrity.storrity.cashaccounts.entity.StatementItemQueryParams;
import com.storrity.storrity.util.sort.SortDirection;
import com.storrity.storrity.util.sort.SortProperty;
import com.storrity.storrity.util.sort.SortPropertyParser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Seun Owa
 */
public class StatementItemRepositoryImpl implements StatementItemRepositoryCustom{

    private final EntityManager em;
    
    @Autowired
    public StatementItemRepositoryImpl(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public List<StatementItem> list(StatementItemQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StatementItem> criteriaQuery = cb.createQuery(StatementItem.class);
        Root<StatementItem> root = criteriaQuery.from(StatementItem.class);
        
        List<Predicate> predicates = buildPredicate(cb, root, params);
        
        // Combine all predicates using AND
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(Predicate[]::new));
        } 
        
        if(params.getSort() != null){           
            List<Order> orders = buildDynamicOrder(cb, root, params); 
            criteriaQuery.orderBy(orders);
        }
        
        TypedQuery<StatementItem> tq = em.createQuery(criteriaQuery);
        
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
    public Long countRecords(StatementItemQueryParams params) {
        
        //basic setup
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<StatementItem> root = cq.from(StatementItem.class);        
        
        //build predicates
        List<Predicate> predicates = buildPredicate(cb, root, params);        
        
        //execute query        
        cq.select(cb.count(root));
        cq.where(predicates.toArray(Predicate[]::new));
        
        return em.createQuery(cq).getSingleResult();
    }
    
    private List<Predicate> buildPredicate(CriteriaBuilder cb, Root<StatementItem> root, StatementItemQueryParams params){
        List<Predicate> predicates = new ArrayList<>();  
        
        if (params.getIds()!= null && !params.getIds().isEmpty()) {
            predicates.add(root.get("id").in(params.getIds()));
        }
        
        if (params.getCashAccountIds()!= null && !params.getCashAccountIds().isEmpty()) {
            predicates.add(root.get("cashAccountId").in(params.getCashAccountIds()));
        }
        
        if (params.getTransactionRefs()!= null && !params.getTransactionRefs().isEmpty()) {
            predicates.add(root.get("transactionRef").in(params.getTransactionRefs()));
        }
        
        if(params.getCashAccountTypes()!=null && !params.getCashAccountTypes().isEmpty()){
            predicates.add(root.get("cashAccountType").in(params.getCashAccountTypes()));
        }

        // Conditionally add date range filter
        if (params.getCreatedAtRange()!= null && params.getCreatedAtRange().size() == 2) {
            predicates.add(cb.between(root.get("createdAt")
                    , params.getCreatedAtRange().get(0)
                    , params.getCreatedAtRange().get(1)));
        }
        
        if (params.getSearchPhrase() != null && !params.getSearchPhrase().isBlank()) {
            String likePattern = "%" + params.getSearchPhrase().toLowerCase() + "%";
            Predicate descriptionLike = cb.like(cb.lower(root.get("description")), likePattern);
            Predicate accountIdLike = cb.like(cb.lower(root.get("cashAccountId")), likePattern);
            predicates.add(cb.or(descriptionLike, accountIdLike));
        }
        
        return predicates;
    }
    
    private List<Order> buildDynamicOrder(CriteriaBuilder cb, Root<StatementItem> root, StatementItemQueryParams params){
            
        // Create a list to hold the Order objects for each sort property
        List<SortProperty> sortProperties = SortPropertyParser.parse(params.getSort());
        return sortProperties.stream()
                .map(sp -> { 
                        if (sp.getSortDirection() == SortDirection.ASC) {
                            return cb.asc(root.get(sp.getPropertyName()));
                        } else {
                            return cb.desc(root.get(sp.getPropertyName()));
                        } 
                    })
                .collect(Collectors.toList());
    }
    
}
