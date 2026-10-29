package com.storrity.storrity.cashier.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
public record SubmitShiftClosingRequest(String closingNotes,@NotEmpty List<@Valid DeclaredPaymentAmount> declaredAmounts) {}
