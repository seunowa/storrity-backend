/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.repository;

import com.storrity.storrity.security.entity.UserPermission;
import com.storrity.storrity.security.entity.UserRole;
import com.storrity.storrity.security.entity.UserRoleQueryParams;
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
public class UserRoleRepositoryImpl implements UserRoleRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserRole> list(UserRoleQueryParams params) {
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserRole> cq = cb.createQuery(UserRole.class);
        Root<UserRole> root = cq.from(UserRole.class);
        root.fetch("permissions", JoinType.LEFT);
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
    public Long countRecords(UserRoleQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<UserRole> root = cq.from(UserRole.class);
        cq.select(cb.count(root)).where(buildPredicates(cb, root, params));
        return em.createQuery(cq).getSingleResult();
    }
    
    
    private Predicate[] buildPredicates(CriteriaBuilder cb, Root<UserRole> root, UserRoleQueryParams params) {
        List<Predicate> predicates = new java.util.ArrayList<>();

        if (!CollectionUtils.isEmpty(params.getRoles())) {
            predicates.add(root.get("id").in(params.getRoles()));
        }
        
        if (!CollectionUtils.isEmpty(params.getPermissions())) {
            Join<UserRole, UserPermission> permissionsJoin = root.join("permissions", JoinType.LEFT);
            predicates.add(permissionsJoin.in(params.getPermissions()));
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
            Predicate  idLike = cb.like(cb.lower(root.get("id")), likePattern);
            Predicate  userNameLike = cb.like(cb.lower(root.get("username")), likePattern);
            predicates.add(cb.or(idLike, userNameLike));
        }

        return predicates.toArray(Predicate[]::new);
    }
}
