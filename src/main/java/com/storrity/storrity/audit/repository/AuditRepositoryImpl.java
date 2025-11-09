/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.audit.repository;

import com.storrity.storrity.audit.entity.Audit;
import com.storrity.storrity.audit.entity.AuditQueryParam;
import com.storrity.storrity.util.sort.SortUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author Seun Owa
 */
public class AuditRepositoryImpl implements AuditRepositoryCustom{

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<Audit> list(AuditQueryParam params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Audit> cq = cb.createQuery(Audit.class);
        Root<Audit> root = cq.from(Audit.class);
        cq.select(root).where(buildPredicates(cb, root, params));
        
        if(params.getSort() != null){           
            List<Order> orders = SortUtils.buildDynamicOrder(cb, root, params.getSort());
            cq.orderBy(orders);
        }

        var query = em.createQuery(cq);

        if (params.getOffset() != null) {
            query.setFirstResult(params.getOffset());
        }

        if (params.getLimit() != null) {
            query.setMaxResults(params.getLimit());
        }

        return query.getResultList();
    }

    @Override
    public Long countRecords(AuditQueryParam params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Audit> root = cq.from(Audit.class);
        cq.select(cb.count(root)).where(buildPredicates(cb, root, params));
        return em.createQuery(cq).getSingleResult();
    }
    
    
    private Predicate[] buildPredicates(CriteriaBuilder cb, Root<Audit> root, AuditQueryParam params) {
        List<Predicate> predicates = new java.util.ArrayList<>();

        if (!CollectionUtils.isEmpty(params.getActionTypes())) {
            predicates.add(root.get("actionType").in(params.getActionTypes()));
        }
        
        if (!CollectionUtils.isEmpty(params.getActions())) {
            predicates.add(root.get("action").in(params.getActions()));
        }
        
        if (!CollectionUtils.isEmpty(params.getIds())) {
            predicates.add(root.get("id").in(params.getIds()));
        }
        
        if (!CollectionUtils.isEmpty(params.getPerformedBy())) {
            predicates.add(root.get("performedBy").in(params.getPerformedBy()));
        }
        
        if (!CollectionUtils.isEmpty(params.getResourceIds())) {
            predicates.add(root.get("resourceId").in(params.getResourceIds()));
        }
        
        if (!CollectionUtils.isEmpty(params.getSourceModue())) {
            predicates.add(root.get("sourceModule").in(params.getSourceModue()));
        }

        if (!CollectionUtils.isEmpty(params.getCreatedAtRange())) {
            predicates.add(cb.between(
                    root.get("createdAt"),
                    params.getCreatedAtRange().get(0),
                    params.getCreatedAtRange().get(1)
            ));
        }

        if (!CollectionUtils.isEmpty(params.getCreatedAtRange())) {
            predicates.add(cb.between(
                    root.get("createdAt"),
                    params.getCreatedAtRange().get(0),
                    params.getCreatedAtRange().get(1)
            ));
        }

        if (!CollectionUtils.isEmpty(params.getUpdatedAtRange())) {
            predicates.add(cb.between(
                    root.get("updatedAt"),
                    params.getUpdatedAtRange().get(0),
                    params.getUpdatedAtRange().get(1)
            ));
        }

        if (params.getSearchPhrase() != null && !params.getSearchPhrase().isBlank()) {
            String likePattern = "%" + params.getSearchPhrase().toLowerCase() + "%";
            Predicate  noteLike = cb.like(cb.lower(root.get("note")), likePattern);
            predicates.add(cb.or(noteLike));
        }

        return predicates.toArray(Predicate[]::new);
    }
    
}
