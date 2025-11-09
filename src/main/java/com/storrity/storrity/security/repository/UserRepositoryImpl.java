/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.repository;

import com.storrity.storrity.security.entity.AppUser;
import com.storrity.storrity.security.entity.AppUserQueryParams;
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
public class UserRepositoryImpl implements UserRepositoryCustom{   

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<AppUser> list(AppUserQueryParams params) {
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AppUser> cq = cb.createQuery(AppUser.class);
        Root<AppUser> root = cq.from(AppUser.class);
        root.fetch("role", JoinType.LEFT);
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
    public Long countRecords(AppUserQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AppUser> root = cq.from(AppUser.class);
        cq.select(cb.count(root)).where(buildPredicates(cb, root, params));
        return em.createQuery(cq).getSingleResult();
    }
    
    
    private Predicate[] buildPredicates(CriteriaBuilder cb, Root<AppUser> root, AppUserQueryParams params) {
        List<Predicate> predicates = new java.util.ArrayList<>();

        if (!CollectionUtils.isEmpty(params.getUsernames())) {
            predicates.add(root.get("username").in(params.getUsernames()));
        }

        if (!CollectionUtils.isEmpty(params.getRoles())) {
            Join<Object, Object> roleJoin = root.join("role");
            predicates.add(roleJoin.get("id").in(params.getRoles()));
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
            Predicate  usernameLike = cb.like(cb.lower(root.get("username")), likePattern);
            predicates.add(cb.or(usernameLike));
        }

        return predicates.toArray(Predicate[]::new);
    }
}
