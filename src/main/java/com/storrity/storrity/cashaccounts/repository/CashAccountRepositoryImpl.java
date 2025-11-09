/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.cashaccounts.repository;

import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.CashAccountQueryParams;
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

/**
 *
 * @author Seun Owa
 */
public class CashAccountRepositoryImpl implements CashAccountRepositoryCustom{
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<CashAccount> list(CashAccountQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CashAccount> criteriaQuery = cb.createQuery(CashAccount.class);
        Root<CashAccount> root = criteriaQuery.from(CashAccount.class);
        
        List<Predicate> predicates = buildPredicate(cb, root, params);
        
        // Combine all predicates using AND
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(Predicate[]::new));
        } 
        
        if(params.getSort() != null){           
            List<Order> orders =  SortUtils.buildDynamicOrder(cb, root, params.getSort());
            criteriaQuery.orderBy(orders);
        }
        
        TypedQuery<CashAccount> tq = em.createQuery(criteriaQuery);
        
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
    public Long countRecords(CashAccountQueryParams params) {
        
        //basic setup
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<CashAccount> root = cq.from(CashAccount.class);        
        
        //build predicates
        List<Predicate> predicates = buildPredicate(cb, root, params);        
        
        //execute query        
        cq.select(cb.count(root));
        cq.where(predicates.toArray(Predicate[]::new));
        
        return em.createQuery(cq).getSingleResult();
    }
    
    private List<Predicate> buildPredicate(CriteriaBuilder cb, Root<CashAccount> root, CashAccountQueryParams params){
        List<Predicate> predicates = new ArrayList<>();        
        
        if(params.getCashAccountTypes()!=null && !params.getCashAccountTypes().isEmpty()){
            predicates.add(root.get("cashAccountType").in(params.getCashAccountTypes()));
        }
        
        if (params.getIds()!= null && !params.getIds().isEmpty()) {
            predicates.add(root.get("id").in(params.getIds()));
        }
        
        if (params.getStatus()!= null && !params.getStatus().isEmpty()) {
            predicates.add(root.get("status").in(params.getStatus()));
        }

        // Conditionally add date range filter
        if (params.getLastTxnAtRange()!= null && params.getLastTxnAtRange().size() == 2) {
            predicates.add(cb.between(root.get("lastTxnAt")
                    , params.getLastTxnAtRange().get(0)
                    , params.getLastTxnAtRange().get(1)));
        }
        
        if (params.getParentAccountIds()!= null && !params.getParentAccountIds().isEmpty()) {
            predicates.add(root.get("parentAccountId").in(params.getParentAccountIds()));
        }

        // Conditionally add date range filter
        if (params.getCreatedAtRange()!= null && params.getCreatedAtRange().size() == 2) {
            predicates.add(cb.between(root.get("createdAt")
                    , params.getCreatedAtRange().get(0)
                    , params.getCreatedAtRange().get(1)));
        }
        
        if (params.getSearchPhrase() != null && !params.getSearchPhrase().isBlank()) {
            String likePattern = "%" + params.getSearchPhrase().toLowerCase() + "%";
            Predicate nameLike = cb.like(cb.lower(root.get("name")), likePattern);
            Predicate accountIdLike = cb.like(cb.lower(root.get("cashAccountId")), likePattern);
            predicates.add(cb.or(nameLike, accountIdLike));
        }
        
        return predicates;
    }
    
}
