package com.storrity.storrity.cashier.service;

import com.storrity.storrity.cashier.dto.*;
import com.storrity.storrity.cashier.entity.*;
import com.storrity.storrity.cashier.repository.*;
import com.storrity.storrity.cart.entity.*;
import com.storrity.storrity.cart.repository.PaymentChannelRepository;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.security.service.AuthenticatedUser;
import com.storrity.storrity.util.dto.CountDto;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
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
    private final PaymentChannelRepository paymentChannelRepo;

    public CashierShiftService(CashierShiftRepository shiftRepo,
            ShiftPaymentReconciliationRepository reconRepo,
            CashFlowRepository flowRepo,
            CheckoutPaymentShiftRepository paymentRepo,
            ShiftClosingAttemptRepository attemptRepo,
            PaymentChannelRepository paymentChannelRepo) {
        this.shiftRepo = shiftRepo;
        this.reconRepo = reconRepo;
        this.flowRepo = flowRepo;
        this.paymentRepo = paymentRepo;
        this.attemptRepo = attemptRepo;
        this.paymentChannelRepo = paymentChannelRepo;
    }
    
    @Transactional(readOnly = true)
    public List<CashierShiftView> listShifts(CashierShiftQueryParams params) {
        return shiftRepo.list(params)
                .stream()
                .map(shift -> view(shift, List.of()))
                .toList();
    }

    @Transactional(readOnly = true)
    public CountDto countShifts(CashierShiftQueryParams params) {
        return CountDto.builder()
                .count(shiftRepo.countRecords(params))
                .build();
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
     * Called inside checkout'shift transaction; the pessimistic lock serializes checkout against shift closing.
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
        CashierShift shift = shiftRepo.findByIdForUpdate(shiftId).orElseThrow(() -> new EntityNotFoundException("Cashier shift not found: " + shiftId));
        requireOpen(shift);
        String username = currentUsername();
        if (req.type() != CashFlowType.CASH_IN && req.type() != CashFlowType.CASH_OUT && req.type() != CashFlowType.CASH_DROP) {
            throw new IllegalArgumentException("Only manual CASH_IN, CASH_OUT, or CASH_DROP flows can be submitted here.");
        }
        boolean inflow = req.type() == CashFlowType.CASH_IN;
        String ref = (req.reference() == null || req.reference().isBlank() ? UUID.randomUUID().toString() : req.reference().trim());
        return flowRepo.save(CashFlow
                .builder()
                .cashierShiftId(shift.getId())
                .storeId(shift.getStoreId())
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
        CashierShift shift = shiftRepo.findByIdForUpdate(shiftId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cashier shift not found: " + shiftId));

        requireOpen(shift);

        List<ExpectedPaymentAmount> expected = calculateExpected(shift);

        Map<Key, ExpectedPaymentAmount> expectedPaymentsMap = expected.stream()
                .collect(Collectors.toMap(
                        x -> new Key(x.paymentMethod(), x.paymentChannelId()),
                        x -> x
                ));

        Set<Key> seen = new HashSet<>();
        List<ShiftPaymentReconciliation> rows = new ArrayList<>();
        String username = currentUsername();

        ShiftClosingAttempt closingAttempt = attemptRepo.save(
                ShiftClosingAttempt.builder()
                        .cashierShiftId(shift.getId())
                        .status(ClosingAttemptStatus.SUBMITTED)
                        .submittedBy(username)
                        .submittedAt(LocalDateTime.now())
                        .closingNotes(req.closingNotes())
                        .build()
        );

        UUID attempt = closingAttempt.getId();

        for (DeclaredPaymentAmount d : req.declaredAmounts()) {

            PaymentChannel channel = paymentChannelRepo.findById(d.paymentChannelId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Payment channel not found: " + d.paymentChannelId()));

            if (!channel.getStoreId().equals(shift.getStoreId())) {
                throw new IllegalArgumentException(
                        "Payment channel does not belong to this shift's store.");
            }

            Key key = new Key(channel.getPaymentMethod(), channel.getId());

            if (!seen.add(key)) {
                throw new IllegalArgumentException(
                        "Duplicate declared payment channel: " + channel.getName());
            }

            ExpectedPaymentAmount e = expectedPaymentsMap.get(key);

            Money expectedAmount = e == null
                    ? Money.ofMicroNaira(0L)
                    : e.expectedAmount();

            rows.add(ShiftPaymentReconciliation.builder()
                    .cashierShiftId(shift.getId())
                    .closingAttemptId(attempt)
                    .paymentMethod(channel.getPaymentMethod())
                    .paymentChannelId(channel.getId())
                    .paymentChannel(channel.getName())
                    .expectedAmount(expectedAmount)
                    .declaredAmount(d.declaredAmount())
                    .variance(d.declaredAmount().subtract(expectedAmount))
                    .notes(d.notes())
                    .submittedBy(username)
                    .build());
        }

        for (Key key : expectedPaymentsMap.keySet()) {
            if (!seen.contains(key)) {
                throw new IllegalArgumentException(
                        "Missing declared amount for expected payment method/channel: " + key);
            }
        }

        reconRepo.saveAll(rows);

        shift.setStatus(CashierShiftStatus.CLOSING_SUBMITTED);
        shift.setClosingSubmittedAt(LocalDateTime.now());
        shift.setClosingSubmittedBy(username);
        shift.setClosingNotes(req.closingNotes());

        shiftRepo.save(shift);

        return view(shift, rows.stream().map(this::view).toList());
    }
    
    @Transactional
    public CashierShiftView closeShift(UUID id) {
        CashierShift shift = shiftRepo.findByIdForUpdate(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier shift not found: " + id));
        if (shift.getStatus() != CashierShiftStatus.CLOSING_SUBMITTED) {
            throw new IllegalStateException("Only a shift with submitted reconciliation can be closed.");
        }
        String reviewer = currentUsername();
        ShiftClosingAttempt attempt = attemptRepo.findFirstByCashierShiftIdOrderBySubmittedAtDesc(id)
                .orElseThrow(() -> new IllegalStateException("Closing attempt not found."));
        if (attempt.getStatus() != ClosingAttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Latest closing attempt is not awaiting review.");
        }
        attempt.setStatus(ClosingAttemptStatus.APPROVED);
        attempt.setReviewedBy(reviewer);
        attempt.setReviewedAt(LocalDateTime.now());
        attemptRepo.save(attempt);
        shift.setStatus(CashierShiftStatus.CLOSED);
        shift.setClosedAt(LocalDateTime.now());
        shift.setClosedBy(reviewer);
        shiftRepo.save(shift);
        return getShift(id);
    }

    @Transactional
    public CashierShiftView rejectClosing(UUID id, String reason) {
        CashierShift shift = shiftRepo.findByIdForUpdate(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier shift not found: " + id));
        if (shift.getStatus() != CashierShiftStatus.CLOSING_SUBMITTED) {
            throw new IllegalStateException("Only a submitted closing can be rejected.");
        }
        String reviewer = currentUsername();
        ShiftClosingAttempt attempt = attemptRepo.findFirstByCashierShiftIdOrderBySubmittedAtDesc(id)
                .orElseThrow(() -> new IllegalStateException("Closing attempt not found."));
        if (attempt.getStatus() != ClosingAttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Latest closing attempt is not awaiting review.");
        }
        attempt.setStatus(ClosingAttemptStatus.REJECTED);
        attempt.setReviewedBy(reviewer);
        attempt.setReviewedAt(LocalDateTime.now());
        attempt.setReviewReason(reason);
        attemptRepo.save(attempt);
        shift.setStatus(CashierShiftStatus.OPEN);
        shift.setClosingSubmittedAt(null);
        shift.setClosingSubmittedBy(null);
        shift.setClosingNotes("Rejected by " + reviewer + ": " + reason);
        shiftRepo.save(shift);
        return getShift(id);
    }

    private List<ExpectedPaymentAmount> calculateExpected(CashierShift shift) {
        Map<Key, Acc> totals = new LinkedHashMap<>();
        
        PaymentChannel cashChannel = paymentChannelRepo
                .findByStoreIdAndCode(shift.getStoreId(), "CASH")
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cash payment channel not configured for this store."
            ));

        Key cashKey = new Key(PaymentMethod.CASH, cashChannel.getId());
        totals.put(cashKey, new Acc(cashChannel.getName(), 0L));
        
        for (CashFlow f : flowRepo.findByCashierShiftIdOrderByCreatedAtAsc(shift.getId())) {
            Acc a = totals.get(cashKey);
            a.amount = Math.addExact(a.amount, f.signedAmountInMicroNaira());
        }
        
        for (CheckoutPayment p : paymentRepo.findByCashierShiftIdAndPaymentStatus(shift.getId(), PaymentStatus.CONFIRMED)) {
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
        return shiftRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier shift not found: " + id));
    }

    private void requireOpen(CashierShift shift) {
        if (!shift.getStatus().equals(CashierShiftStatus.OPEN)) {
            throw new IllegalStateException("Cashier shift is not open.");
        }
    }

    private String currentUsername() {
//        @Todo refacto to use utility to avoid code repeteation across several classes
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
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

    private CashierShiftView view(CashierShift shift, List<PaymentReconciliationView> reconciliations) {
        return new CashierShiftView(
                shift.getId(),
                shift.getStoreId(),
                shift.getCashierUsername(),
                shift.getStatus(),
                shift.getOpeningFloat(),
                shift.getOpenedAt(),
                shift.getOpenedBy(),
                shift.getClosingSubmittedAt(),
                shift.getClosingSubmittedBy(),
                shift.getClosedAt(),
                shift.getClosedBy(),
                shift.getClosingNotes(),
                reconciliations);
    }

    private record Key(PaymentMethod method, UUID channel) {

    }

    private static final class Acc {

        final String name;
        long amount;

        Acc(String name, long amount) {
            this.name = name;
            this.amount = amount;
        }
    }
}
