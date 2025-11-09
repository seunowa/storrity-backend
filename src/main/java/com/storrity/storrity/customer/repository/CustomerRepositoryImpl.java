/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.customer.repository;

import com.storrity.storrity.customer.entity.Customer;
import com.storrity.storrity.customer.entity.CustomerQueryParams;
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
public class CustomerRepositoryImpl implements CustomerRepositoryCustom{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Customer> list(CustomerQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = cb.createQuery(Customer.class);
        Root<Customer> root = criteriaQuery.from(Customer.class);
        
        List<Predicate> predicates = buildPredicate(cb, root, params);
        
        // Combine all predicates using AND
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(Predicate[]::new));
        } 
        
        if(params.getSort() != null){           
            List<Order> orders = SortUtils.buildDynamicOrder(cb, root, params.getSort());
            criteriaQuery.orderBy(orders);
        }
        
        TypedQuery<Customer> tq = em.createQuery(criteriaQuery);
        
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
    public Long countRecords(CustomerQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Customer> root = cq.from(Customer.class);        
        
        //build predicates
        List<Predicate> predicates = buildPredicate(cb, root, params);        
        
        //execute query        
        cq.select(cb.count(root));
        cq.where(predicates.toArray(Predicate[]::new));
        
        return em.createQuery(cq).getSingleResult();
    }

    private List<Predicate> buildPredicate(CriteriaBuilder cb, Root<Customer> root, CustomerQueryParams params) {
        List<Predicate> predicates = new ArrayList<>();        
                
        if (params.getIds()!= null && !params.getIds().isEmpty()) {
            predicates.add(root.get("id").in(params.getIds()));
        }      
                
        if (params.getFullNames()!= null && !params.getFullNames().isEmpty()) {
            predicates.add(root.get("fullName").in(params.getFullNames()));
        }      
                
        if (params.getPhones()!= null && !params.getPhones().isEmpty()) {
            predicates.add(root.get("phone").in(params.getPhones()));
        }      
                
        if (params.getEmails()!= null && !params.getEmails().isEmpty()) {
            predicates.add(root.get("email").in(params.getEmails()));
        }      
                
        if (params.getStates()!= null && !params.getStates().isEmpty()) {
            predicates.add(root.get("state").in(params.getStates()));
        }      
                
        if (params.getCities()!= null && !params.getCities().isEmpty()) {
            predicates.add(root.get("city").in(params.getCities()));
        }      
                
        if (params.getStreets()!= null && !params.getStreets().isEmpty()) {
            predicates.add(root.get("street").in(params.getStreets()));
        }      
                
        if (params.getMainCashAccountIds()!= null && !params.getMainCashAccountIds().isEmpty()) {
            predicates.add(root.get("mainCashAccountId").in(params.getMainCashAccountIds()));
        }      
                
        if (params.getCreditCashAccounIds()!= null && !params.getCreditCashAccounIds().isEmpty()) {
            predicates.add(root.get("creditCashAccounId").in(params.getCreditCashAccounIds()));
        }      
                
        if (params.getRewardCashAccountIds()!= null && !params.getRewardCashAccountIds().isEmpty()) {
            predicates.add(root.get("rewardCashAccountId").in(params.getRewardCashAccountIds()));
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
            predicates.add(cb.or(nameLike));
        }
        
        return predicates;
    }
}
