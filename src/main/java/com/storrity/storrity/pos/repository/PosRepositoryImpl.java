/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.pos.repository;

import com.storrity.storrity.pos.entity.Pos;
import com.storrity.storrity.pos.entity.PosQueryParam;
import com.storrity.storrity.util.sort.SortUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author Seun Owa
 */
@Repository
public class PosRepositoryImpl implements PosRepositoryCustom{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Pos> list(PosQueryParam params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Pos> cq = cb.createQuery(Pos.class);
        Root<Pos> root = cq.from(Pos.class);
        root.fetch("store", JoinType.LEFT);
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
    public Long countRecords(PosQueryParam params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Pos> root = cq.from(Pos.class);
        cq.select(cb.count(root)).where(buildPredicates(cb, root, params));
        return em.createQuery(cq).getSingleResult();
    }
    
    private Predicate[] buildPredicates(CriteriaBuilder cb, Root<Pos> root, PosQueryParam params) {
        List<Predicate> predicates = new java.util.ArrayList<>();

        if (!CollectionUtils.isEmpty(params.getIds())) {
            predicates.add(root.get("id").in(params.getIds()));
        }
        
        if (!CollectionUtils.isEmpty(params.getNames())) {
            predicates.add(root.get("name").in(params.getNames()));
        }
        
        if (!CollectionUtils.isEmpty(params.getIdentifiers())) {
            predicates.add(root.get("identifier").in(params.getIdentifiers()));
        }
        
        if (!CollectionUtils.isEmpty(params.getStatus())) {
            predicates.add(root.get("status").in(params.getStatus()));
        }

        if (!CollectionUtils.isEmpty(params.getStores())) {
            Join<Object, Object> storeJoin = root.join("store");
            predicates.add(storeJoin.get("id").in(params.getStores()));
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
            predicates.add(cb.like(cb.lower(root.get("name")), likePattern));
        }

        return predicates.toArray(Predicate[]::new);
    }
}
