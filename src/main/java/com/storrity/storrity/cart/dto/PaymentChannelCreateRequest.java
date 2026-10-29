package com.storrity.storrity.cart.dto;

import com.storrity.storrity.cart.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentChannelCreateRequest {
    @NotNull
    private UUID storeId;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String code;
}
