package com.storrity.storrity.cashier.repository;

import com.storrity.storrity.cashier.entity.CashierShift;
import com.storrity.storrity.cashier.entity.CashierShiftQueryParams;
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
public class CashierShiftRepositoryImpl implements CashierShiftRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CashierShift> list(CashierShiftQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CashierShift> cq = cb.createQuery(CashierShift.class);
        Root<CashierShift> root = cq.from(CashierShift.class);

        List<Predicate> predicates = buildPredicate(cb, root, params);

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(Predicate[]::new));
        }

        if (params.getSort() != null && !params.getSort().isBlank()) {
            List<Order> orders = SortUtils.buildDynamicOrder(cb, root, params.getSort());
            cq.orderBy(orders);
        }

        TypedQuery<CashierShift> query = em.createQuery(cq);

        if (params.getOffset() != null) {
            query.setFirstResult(params.getOffset());
        }

        if (params.getLimit() != null) {
            query.setMaxResults(params.getLimit());
        }

        return query.getResultList();
    }

    @Override
    public Long countRecords(CashierShiftQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<CashierShift> root = cq.from(CashierShift.class);

        List<Predicate> predicates = buildPredicate(cb, root, params);

        cq.select(cb.count(root));

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(Predicate[]::new));
        }

        return em.createQuery(cq).getSingleResult();
    }

    private List<Predicate> buildPredicate(
            CriteriaBuilder cb,
            Root<CashierShift> root,
            CashierShiftQueryParams params) {

        List<Predicate> predicates = new ArrayList<>();

        if (params.getId() != null) {
            predicates.add(cb.equal(root.get("id"), params.getId()));
        }

        if (params.getStoreId() != null) {
            predicates.add(cb.equal(root.get("storeId"), params.getStoreId()));
        }

        if (params.getCashierUsername() != null
                && !params.getCashierUsername().isBlank()) {
            predicates.add(cb.equal(
                    cb.lower(root.get("cashierUsername")),
                    params.getCashierUsername().trim().toLowerCase()
            ));
        }

        if (params.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), params.getStatus()));
        }

        if (params.getOpenedAtRange() != null
                && params.getOpenedAtRange().size() == 2) {
            predicates.add(cb.between(
                    root.get("openedAt"),
                    params.getOpenedAtRange().get(0),
                    params.getOpenedAtRange().get(1)
            ));
        }

        if (params.getClosedAtRange() != null
                && params.getClosedAtRange().size() == 2) {
            predicates.add(cb.between(
                    root.get("closedAt"),
                    params.getClosedAtRange().get(0),
                    params.getClosedAtRange().get(1)
            ));
        }

        if (params.getSearchPhrase() != null
                && !params.getSearchPhrase().isBlank()) {
            String search = "%" + params.getSearchPhrase().trim().toLowerCase() + "%";

            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("cashierUsername")), search),
                    cb.like(cb.lower(root.get("closingNotes")), search),
                    cb.like(cb.lower(root.get("openedBy")), search),
                    cb.like(cb.lower(root.get("closedBy")), search)
            ));
        }

        return predicates;
    }
}