/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.supply.repository;

import com.storrity.storrity.supply.dto.SupplyQueryParams;
import com.storrity.storrity.supply.entity.Supply;
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
public class SupplyRepositoryImpl implements SupplyRepositoryCustom{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Supply> list(SupplyQueryParams params) {        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Supply> criteriaQuery = cb.createQuery(Supply.class);
        Root<Supply> root = criteriaQuery.from(Supply.class);
        
        List<Predicate> predicates = buildPredicate(cb, root, params);
        
        // Combine all predicates using AND
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(Predicate[]::new));
        } 
        
        if(params.getSort() != null){           
            List<Order> orders = SortUtils.buildDynamicOrder(cb, root, params.getSort());
            criteriaQuery.orderBy(orders);
        }
        
        TypedQuery<Supply> tq = em.createQuery(criteriaQuery);
        
        //if offest is provided apply offset
        if( null != params.getOffset()){
            tq.setFirstResult(params.getOffset());
        }
        
        //if limit is provided apply limit
        if( null != params.getLimit()){
            tq.setMaxResults(params.getLimit());
        }
        
        return tq.getResultList();}

    @Override
    public Long countRecords(SupplyQueryParams params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Supply> root = cq.from(Supply.class);        
        
        //build predicates
        List<Predicate> predicates = buildPredicate(cb, root, params);        
        
        //execute query        
        cq.select(cb.count(root));
        cq.where(predicates.toArray(Predicate[]::new));
        
        return em.createQuery(cq).getSingleResult();
    }
    
    private List<Predicate> buildPredicate(CriteriaBuilder cb, Root<Supply> root, SupplyQueryParams params) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (params.getIds()!= null && !params.getIds().isEmpty()) {
            predicates.add(root.get("id").in(params.getIds()));
        }
        
        if (params.getSupplyReferences()!= null && !params.getSupplyReferences().isEmpty()) {
            predicates.add(root.get("supplyReference").in(params.getSupplyReferences()));
        }
        
        if (params.getStoreIds()!= null && !params.getStoreIds().isEmpty()) {
//            predicates.add(root.get("store").in(params.getStoreIds()));
            predicates.add(root.get("store").get("id").in(params.getStoreIds()));
        }

        // Conditionally add date range filter
        if (params.getSupplyDateRange()!= null && params.getSupplyDateRange().size() == 2) {
            predicates.add(cb.between(root.get("supplyDate")
                    , params.getCreatedAtRange().get(0)
                    , params.getCreatedAtRange().get(1)));
        }
        
        if (params.getEnteredByUserIds()!= null && !params.getEnteredByUserIds().isEmpty()) {
            predicates.add(root.get("enteredByUserId").in(params.getEnteredByUserIds()));
        }
        
        if (params.getReceivedByUserIds()!= null && !params.getReceivedByUserIds().isEmpty()) {
            predicates.add(root.get("receivedByUserId").in(params.getReceivedByUserIds()));
        }
        
        if (params.getSupplyStatus()!= null && !params.getSupplyStatus().isEmpty()) {
            predicates.add(root.get("supplyStatus").in(params.getSupplyStatus()));
        }
        
        if (params.getDeliveryNoteNumbers()!= null && !params.getDeliveryNoteNumbers().isEmpty()) {
            predicates.add(root.get("deliveryNoteNumber").in(params.getDeliveryNoteNumbers()));
        }
        
        if (params.getInvoiceNumbers()!= null && !params.getInvoiceNumbers().isEmpty()) {
            predicates.add(root.get("invoiceNumber").in(params.getInvoiceNumbers()));
        }
        
        if (params.getSupplierIds()!= null && !params.getSupplierIds().isEmpty()) {
            predicates.add(root.get("supplierId").in(params.getSupplierIds()));
        }
        
        if (params.getSupplierNames()!= null && !params.getSupplierNames().isEmpty()) {
            predicates.add(root.get("supplierName").in(params.getSupplierNames()));
        }
        
        if (params.getContactPersons()!= null && !params.getContactPersons().isEmpty()) {
            predicates.add(root.get("contactPerson").in(params.getContactPersons()));
        }
        
        if (params.getSupplierPhones()!= null && !params.getSupplierPhones().isEmpty()) {
            predicates.add(root.get("supplierPhones").in(params.getSupplierPhones()));
        }
        
        if (params.getSupplierEmails()!= null && !params.getSupplierEmails().isEmpty()) {
            predicates.add(root.get("supplierEmails").in(params.getSupplierEmails()));
        }
        
        if (params.getPaymentStatus()!= null) {
            predicates.add(root.get("paymentStatus").in(params.getPaymentStatus()));
        }
        
        if (params.getPaymentMethods()!= null && !params.getPaymentMethods().isEmpty()) {
            predicates.add(root.get("paymentMethod").in(params.getSupplierEmails()));
        }
        
        if (params.getApprovedByUserIds()!= null && !params.getApprovedByUserIds().isEmpty()) {
            predicates.add(root.get("approvedByUserIds").in(params.getApprovedByUserIds()));
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
