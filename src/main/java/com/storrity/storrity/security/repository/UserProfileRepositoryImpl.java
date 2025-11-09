/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.security.repository;

import com.storrity.storrity.security.entity.AppUser;
import com.storrity.storrity.security.entity.UserProfile;
import com.storrity.storrity.security.entity.UserProfileQueryParams;
import com.storrity.storrity.util.sort.SortUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
public class UserProfileRepositoryImpl implements UserProfileRepositoryCustom{   

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserProfile> list(UserProfileQueryParams params) {
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserProfile> cq = cb.createQuery(UserProfile.class);
        Root<UserProfile> root = cq.from(UserProfile.class);
        
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
    public Long countRecords(UserProfileQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<UserProfile> root = cq.from(UserProfile.class);
        cq.select(cb.count(root)).where(buildPredicates(cb, root, params));
        return em.createQuery(cq).getSingleResult();
    }
    
    
    private Predicate[] buildPredicates(CriteriaBuilder cb, Root<UserProfile> root, UserProfileQueryParams params) {
        List<Predicate> predicates = new java.util.ArrayList<>();

        if (!CollectionUtils.isEmpty(params.getUsernames())) {
            predicates.add(root.get("username").in(params.getUsernames()));
        }
        
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
