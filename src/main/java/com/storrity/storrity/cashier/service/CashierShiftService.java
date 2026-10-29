package com.storrity.storrity.cashier.service;

import com.storrity.storrity.cashier.dto.*;
import com.storrity.storrity.cashier.entity.*;
import com.storrity.storrity.cashier.repository.*;
import com.storrity.storrity.cart.entity.*;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.security.service.AuthenticatedUser;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CashierShiftService {

    private final CashierShiftRepository shiftRepo;
    private final ShiftPaymentReconciliationRepository reconRepo;
    private final CashFlowRepository flowRepo;
    private final CheckoutPaymentShiftRepository paymentRepo;
    private final ShiftClosingAttemptRepository attemptRepo;

    public CashierShiftService(CashierShiftRepository s,
            ShiftPaymentReconciliationRepository r,
            CashFlowRepository f,
            CheckoutPaymentShiftRepository p,
            ShiftClosingAttemptRepository a) {
        shiftRepo = s;
        reconRepo = r;
        flowRepo = f;
        paymentRepo = p;
        attemptRepo = a;
    }

    @Transactional
    public CashierShiftView openShift(OpenCashierShiftRequest req) {
        String username = currentUsername();
        List<CashierShiftStatus> active = List.of(CashierShiftStatus.OPEN, CashierShiftStatus.CLOSING_SUBMITTED);
        if (shiftRepo.existsByStoreIdAndCashierUsernameAndStatusIn(req.storeId(), username, active)) {
            throw new IllegalStateException("You already have an active shift for this store.");
        }
        Money openingFloat = req.openingFloat() == null ? Money.ofMicroNaira(0L) : req.openingFloat();
        if (openingFloat.getValueInMicroNaira() < 0) {
            throw new IllegalArgumentException("Opening float cannot be negative.");
        }
        CashierShift shift = CashierShift
                .builder()
                .storeId(req.storeId())
                .cashierUsername(username)
                .cashierUsername(username)
                .status(CashierShiftStatus.OPEN)
                .openingFloat(req.openingFloat())
                .openedAt(LocalDateTime.now())
                .openedBy(username)
                .build();
        shift = shiftRepo.saveAndFlush(shift);
        
        if (req.openingFloat().isPositiveValue()) {
            flowRepo.save(CashFlow
                    .builder()
                    .cashierShiftId(shift.getId())
                    .storeId(shift.getStoreId())
                    .flowType(CashFlowType.OPENING_FLOAT)
                    .inflow(openingFloat)
                    .outflow(Money.ofMicroNaira(0L))
                    .sourceReference("SHIFT-OPENING:" + shift.getId())
                    .transactionRef(shift.getId().toString())
                    .description("Opening float")
                    .performedBy(username)
                    .build());
        }
        return view(shift, List.of());
    }

    @Transactional(readOnly = true)
    public CashierShiftView getShift(UUID id) {
        CashierShift s = required(id);
        return view(s, reconRepo.findByCashierShiftIdOrderBySubmittedAtDesc(id).stream().map(this::view).toList());
    }

    @Transactional(readOnly = true)
    public List<ExpectedPaymentAmount> expectedAmounts(UUID id) {
        return calculateExpected(required(id));
    }

    /**
     * Called inside checkout's transaction; the pessimistic lock serializes
     * checkout against shift closing.
     * @param storeId
     * @param username
     * @return 
     */
    @Transactional
    public CashierShift requireOpenShiftForCheckout(UUID storeId, String username) {        
        CashierShift shift = shiftRepo.findOpenForUpdate(storeId, username, CashierShiftStatus.OPEN)
                .orElseThrow(() -> new IllegalStateException("No open cashier shift exists for this user and store."));
        return shift;
    }

    @Transactional
    public CashFlow recordManualFlow(UUID shiftId, CashFlowRequest req) {
        CashierShift s = shiftRepo.findByIdForUpdate(shiftId).orElseThrow(() -> new EntityNotFoundException("Cashier shift not found: " + shiftId));
        requireOpen(s);
        String username = currentUsername();
        if (req.type() != CashFlowType.CASH_IN && req.type() != CashFlowType.CASH_OUT && req.type() != CashFlowType.CASH_DROP) {
            throw new IllegalArgumentException("Only manual CASH_IN, CASH_OUT, or CASH_DROP flows can be submitted here.");
        }
        boolean inflow = req.type() == CashFlowType.CASH_IN;
        String ref = (req.reference() == null || req.reference().isBlank() ? UUID.randomUUID().toString() : req.reference().trim());
        return flowRepo.save(CashFlow
                .builder()
                .cashierShiftId(s.getId())
                .storeId(s.getStoreId())
                .flowType(req.type())
                .inflow(inflow ? req.amount() : Money.ofMicroNaira(0L))
                .outflow(inflow ? Money.ofMicroNaira(0L) : req.amount())
                .sourceReference("MANUAL:" + ref)
                .description(req.description())
                .performedBy(username)
                .build());
    }

    @Transactional
    public CashierShiftView submitClosing(UUID shiftId, SubmitShiftClosingRequest req) {
        CashierShift s = shiftRepo.findByIdForUpdate(shiftId)
                .orElseThrow(() -> new EntityNotFoundException("Cashier shift not found: " + shiftId));
        
        requireOpen(s);
        
        List<ExpectedPaymentAmount> expected = calculateExpected(s);
        
        Map<Key, ExpectedPaymentAmount> byKey = expected
                .stream()
                .collect(Collectors.toMap(x -> new Key(x.paymentMethod(), x.paymentChannelId()), x -> x));
        Set<Key> seen = new HashSet<>();
        List<ShiftPaymentReconciliation> rows = new ArrayList<>();
        String username = currentUsername();
        ShiftClosingAttempt closingAttempt = attemptRepo.save(ShiftClosingAttempt
                .builder()
                .cashierShiftId(s.getId())
                .status(ClosingAttemptStatus.SUBMITTED)
                .submittedBy(username)
                .submittedAt(LocalDateTime.now())
                .closingNotes(req.closingNotes())
                .build());
        UUID attempt = closingAttempt.getId();
        
        for (DeclaredPaymentAmount d : req.declaredAmounts()) {
            Key k = new Key(d.paymentMethod(), d.paymentChannelId());
            if (!seen.add(k)) {
                throw new IllegalArgumentException("Duplicate declared payment method/channel: " + k);
            }
            ExpectedPaymentAmount e = byKey.get(k);
            Money exp = e == null ? Money.ofMicroNaira(0L) : e.expectedAmount();
            rows.add(ShiftPaymentReconciliation
                    .builder()
                    .cashierShiftId(s.getId())
                    .closingAttemptId(attempt)
                    .paymentMethod(d.paymentMethod())
                    .paymentChannelId(d.paymentChannelId())
                    .paymentChannel(d.paymentChannel())
                    .expectedAmount(exp)
                    .declaredAmount(d.declaredAmount())
                    .variance(d.declaredAmount().subtract(exp))
                    .notes(d.notes())
                    .submittedBy(username)
                    .build());
        }
        for (Key k : byKey.keySet()) {
            if (!seen.contains(k)) {
                throw new IllegalArgumentException("Missing declared amount for expected payment method/channel: " + k);
            }
        }
        reconRepo.saveAll(rows);
        s.setStatus(CashierShiftStatus.CLOSING_SUBMITTED);
        s.setClosingSubmittedAt(LocalDateTime.now());
        s.setClosingSubmittedBy(username);
        s.setClosingNotes(req.closingNotes());
        shiftRepo.save(s);
        return view(s, rows.stream().map(this::view).toList());
    }

    @Transactional
    public CashierShiftView closeShift(UUID id) {
        CashierShift s = shiftRepo.findByIdForUpdate(id).orElseThrow(() -> new EntityNotFoundException("Cashier shift not found: " + id));
        if (s.getStatus() != CashierShiftStatus.CLOSING_SUBMITTED) {
            throw new IllegalStateException("Only a shift with submitted reconciliation can be closed.");
        }
        String reviewer = currentUsername();
        ShiftClosingAttempt a = attemptRepo.findFirstByCashierShiftIdOrderBySubmittedAtDesc(id).orElseThrow(() -> new IllegalStateException("Closing attempt not found."));
        if (a.getStatus() != ClosingAttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Latest closing attempt is not awaiting review.");
        }
        a.setStatus(ClosingAttemptStatus.APPROVED);
        a.setReviewedBy(reviewer);
        a.setReviewedAt(LocalDateTime.now());
        attemptRepo.save(a);
        s.setStatus(CashierShiftStatus.CLOSED);
        s.setClosedAt(LocalDateTime.now());
        s.setClosedBy(reviewer);
        shiftRepo.save(s);
        return getShift(id);
    }

    @Transactional
    public CashierShiftView rejectClosing(UUID id, String reason) {
        CashierShift s = shiftRepo.findByIdForUpdate(id).orElseThrow(() -> new EntityNotFoundException("Cashier shift not found: " + id));
        if (s.getStatus() != CashierShiftStatus.CLOSING_SUBMITTED) {
            throw new IllegalStateException("Only a submitted closing can be rejected.");
        }
        String reviewer = currentUsername();
        ShiftClosingAttempt a = attemptRepo.findFirstByCashierShiftIdOrderBySubmittedAtDesc(id).orElseThrow(() -> new IllegalStateException("Closing attempt not found."));
        if (a.getStatus() != ClosingAttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Latest closing attempt is not awaiting review.");
        }
        a.setStatus(ClosingAttemptStatus.REJECTED);
        a.setReviewedBy(reviewer);
        a.setReviewedAt(LocalDateTime.now());
        a.setReviewReason(reason);
        attemptRepo.save(a);
        s.setStatus(CashierShiftStatus.OPEN);
        s.setClosingSubmittedAt(null);
        s.setClosingSubmittedBy(null);
        s.setClosingNotes("Rejected by " + reviewer + ": " + reason);
        shiftRepo.save(s);
        return getShift(id);
    }

    private List<ExpectedPaymentAmount> calculateExpected(CashierShift s) {
        Map<Key, Acc> totals = new LinkedHashMap<>();
        totals.put(new Key(PaymentMethod.CASH, null), new Acc("Cash", 0L));
        for (CashFlow f : flowRepo.findByCashierShiftIdOrderByCreatedAtAsc(s.getId())) {
            Key k = new Key(PaymentMethod.CASH, null);
            Acc a = totals.computeIfAbsent(k, x -> new Acc("Cash", 0));
            a.amount = Math.addExact(a.amount, f.signedAmountInMicroNaira());
        }
        for (CheckoutPayment p : paymentRepo.findByCashierShiftIdAndPaymentStatus(s.getId(), PaymentStatus.CONFIRMED)) {
            if (p.getPaymentMethod() == PaymentMethod.CASH) {
                continue;
            }
            Key k = new Key(p.getPaymentMethod(), p.getPaymentChannelId());
            String name = p.getPaymentChannel() == null ? p.getPaymentMethod().name() : p.getPaymentChannel();
            Acc a = totals.computeIfAbsent(k, x -> new Acc(name, 0));
            a.amount = Math.addExact(a.amount, p.getAmount().getValueInMicroNaira());
        }
        return totals
                .entrySet()
                .stream()
                .map(e -> new ExpectedPaymentAmount(e.getKey().method, e.getKey().channel, e.getValue().name, Money.ofMicroNaira(e.getValue().amount)))
                .toList();
    }

    private CashierShift required(UUID id) {
        return shiftRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Cashier shift not found: " + id));
    }

    private void requireOpen(CashierShift s) {
        if (!s.getStatus().equals(CashierShiftStatus.OPEN)) {
            throw new IllegalStateException("Cashier shift is not open.");
        }
    }

    private String currentUsername() {
//        @Todo refacto to use utility to avoid code repeteation across several classes
        var a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || !(a.getPrincipal() instanceof AuthenticatedUser u)) {
            throw new IllegalStateException("Authenticated user is required.");
        }
        return u.getUsername();
    }

    private PaymentReconciliationView view(ShiftPaymentReconciliation r) {
        return new PaymentReconciliationView(r.getId(), r.getCashierShiftId(), r.getClosingAttemptId(),
                r.getPaymentMethod(), r.getPaymentChannelId(), r.getPaymentChannel(),
                r.getExpectedAmount(), r.getDeclaredAmount(),
                r.getVariance(), r.getNotes(), r.getSubmittedBy(),
                r.getSubmittedAt());
    }

    private CashierShiftView view(CashierShift s, List<PaymentReconciliationView> r) {
        return new CashierShiftView(
                s.getId(),
                s.getStoreId(),
                s.getCashierUsername(),
                s.getStatus(),
                s.getOpeningFloat(),
                s.getOpenedAt(),
                s.getOpenedBy(),
                s.getClosingSubmittedAt(),
                s.getClosingSubmittedBy(),
                s.getClosedAt(),
                s.getClosedBy(),
                s.getClosingNotes(),
                r);
    }

    private record Key(PaymentMethod method, UUID channel) {

    }

    private static final class Acc {

        final String name;
        long amount;

        Acc(String n, long a) {
            name = n;
            amount = a;
        }
    }
}
