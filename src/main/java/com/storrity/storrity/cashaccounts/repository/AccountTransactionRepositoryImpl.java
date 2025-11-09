/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.repository;

import com.storrity.storrity.cashaccounts.entity.AccountTransaction;
import com.storrity.storrity.cashaccounts.entity.AccountTransactionQueryParams;
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
import org.springframework.stereotype.Repository;

/**
 *
 * @author Seun Owa
 */
@Repository
public class AccountTransactionRepositoryImpl implements AccountTransactionRepositoryCustom{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<AccountTransaction> list(AccountTransactionQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AccountTransaction> criteriaQuery = cb.createQuery(AccountTransaction.class);
        Root<AccountTransaction> root = criteriaQuery.from(AccountTransaction.class);
        
        List<Predicate> predicates = buildPredicate(cb, root, params);
        
        // Combine all predicates using AND
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(Predicate[]::new));
        } 
        
        if(params.getSort() != null){           
            List<Order> orders =  SortUtils.buildDynamicOrder(cb, root, params.getSort());
            criteriaQuery.orderBy(orders);
        }
        
        TypedQuery<AccountTransaction> tq = em.createQuery(criteriaQuery);
        
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
    public Long countRecords(AccountTransactionQueryParams params) {
        
        //basic setup
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AccountTransaction> root = cq.from(AccountTransaction.class);        
        
        //build predicates
        List<Predicate> predicates = buildPredicate(cb, root, params);        
        
        //execute query        
        cq.select(cb.count(root));
        cq.where(predicates.toArray(Predicate[]::new));
        
        return em.createQuery(cq).getSingleResult();
    }
    
    private List<Predicate> buildPredicate(CriteriaBuilder cb, Root<AccountTransaction> root, AccountTransactionQueryParams params) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (params.getIds()!= null && !params.getIds().isEmpty()) {
            predicates.add(root.get("id").in(params.getIds()));
        }
        
        if (params.getTransactionRefs()!= null && !params.getTransactionRefs().isEmpty()) {
            predicates.add(root.get("transactionRef").in(params.getTransactionRefs()));
        }
        
        if (params.getPerformedBy()!= null && !params.getPerformedBy().isEmpty()) {
            predicates.add(root.get("performedBy").in(params.getPerformedBy()));
        }
        
        if (params.getTags()!= null && !params.getTags().isEmpty()) {
            predicates.add(root.get("tags").in(params.getTags()));
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
            Predicate notesLike = cb.like(cb.lower(root.get("notes")), likePattern);
            predicates.add(cb.or(notesLike));
        }
        
        return predicates;
    }
}
