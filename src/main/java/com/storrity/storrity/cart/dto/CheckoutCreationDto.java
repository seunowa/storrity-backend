package com.storrity.storrity.cart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.storrity.storrity.cashaccounts.entity.Money;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
//@Todo implement validation for this object such the the controller can use @Valid to confirm it has the correct values
//expecially for the nested objects (change/amount/amountTendered)
public class CheckoutCreationDto {
    @NotNull
    private UUID cartId;
//    private String paymentMethod;
    @NotEmpty
    @Valid
    private List<CheckoutPaymentDto> payments;
    private List<ChangeDto> changes;
    @NotNull
    private Money amountPaid;
    @NotNull
    private UUID shiftId;
    
    /**
     * Confirms the sum of all payment amounts equals amountPaid.
     * Runs automatically when the DTO is validated with @Valid.
     */
    @JsonIgnore
    @AssertTrue(message = "The sum of payments must equal the amount paid")
    public boolean isPaymentsSumEqualToAmountPaid() {
        // Missing fields are reported by @NotNull / @NotEmpty, so skip here
        if (amountPaid == null || payments == null || payments.isEmpty()) {
            return true;
        }

        Money total = new Money(0L);
        for (CheckoutPaymentDto payment : payments) {
            if (payment == null || payment.getAmount()== null) {
                return false;
            }
            total = total.add(payment.getAmount());
        }

        return total.equals(amountPaid);
    }
}
