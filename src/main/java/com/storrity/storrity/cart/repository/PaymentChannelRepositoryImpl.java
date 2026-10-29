package com.storrity.storrity.cart.repository;

import com.storrity.storrity.cart.entity.PaymentChannel;
import com.storrity.storrity.cart.entity.PaymentChannelQueryParams;
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
public class PaymentChannelRepositoryImpl implements PaymentChannelRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<PaymentChannel> list(PaymentChannelQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PaymentChannel> cq = cb.createQuery(PaymentChannel.class);
        Root<PaymentChannel> root = cq.from(PaymentChannel.class);
        List<Predicate> predicates = buildPredicates(cb, root, params);
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(Predicate[]::new));
        }
        if (params.getSort() != null && !params.getSort().isBlank()) {
            List<Order> orders = SortUtils.buildDynamicOrder(cb, root, params.getSort());
            cq.orderBy(orders);
        }
        TypedQuery<PaymentChannel> query = em.createQuery(cq);
        if (params.getOffset() != null) {
            query.setFirstResult(params.getOffset());
        }
        if (params.getLimit() != null) {
            query.setMaxResults(params.getLimit());
        }
        return query.getResultList();
    }

    @Override
    public Long countRecords(PaymentChannelQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<PaymentChannel> root = cq.from(PaymentChannel.class);
        cq.select(cb.count(root));
        List<Predicate> predicates = buildPredicates(cb, root, params);
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(Predicate[]::new));
        }
        return em.createQuery(cq).getSingleResult();
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb, Root<PaymentChannel> root, PaymentChannelQueryParams params) {
        List<Predicate> predicates = new ArrayList<>();
        if (params.getIds() != null && !params.getIds().isEmpty()) {
            predicates.add(root.get("id").in(params.getIds()));
        }
        if (params.getStoreIds() != null && !params.getStoreIds().isEmpty()) {
            predicates.add(root.get("storeId").in(params.getStoreIds()));
        }
        if (params.getPaymentMethods() != null && !params.getPaymentMethods().isEmpty()) {
            predicates.add(root.get("paymentMethod").in(params.getPaymentMethods()));
        }
        if (params.getCodes() != null && !params.getCodes().isEmpty()) {
            predicates.add(root.get("code").in(params.getCodes()));
        }
        if (params.getNames() != null && !params.getNames().isEmpty()) {
            predicates.add(root.get("name").in(params.getNames()));
        }
        if (params.getActive() != null) {
            predicates.add(cb.equal(root.get("active"), params.getActive()));
        }
        if (params.getCreatedAtRange() != null && params.getCreatedAtRange().size() == 2) {
            predicates.add(cb.between(root.get("createdAt"),
                    params.getCreatedAtRange().get(0), params.getCreatedAtRange().get(1)));
        }
        if (params.getUpdatedAtRange() != null && params.getUpdatedAtRange().size() == 2) {
            predicates.add(cb.between(root.get("updatedAt"),
                    params.getUpdatedAtRange().get(0), params.getUpdatedAtRange().get(1)));
        }
        if (params.getSearchPhrase() != null && !params.getSearchPhrase().isBlank()) {
            String pattern = "%" + params.getSearchPhrase().trim().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("code")), pattern)));
        }
        return predicates;
    }
}
