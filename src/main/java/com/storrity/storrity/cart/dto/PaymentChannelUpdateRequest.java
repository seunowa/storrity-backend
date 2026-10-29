package com.storrity.storrity.cart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentChannelUpdateRequest {
    @NotBlank
    @Size(max = 100)
    private String name;
}
