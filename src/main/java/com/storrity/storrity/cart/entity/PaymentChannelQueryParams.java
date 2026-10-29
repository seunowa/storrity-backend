package com.storrity.storrity.cart.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class PaymentChannelQueryParams {
    private List<UUID> ids;
    @NotEmpty(message = "storeIds must contain at least one store the user is authorized to access")
    private List<UUID> storeIds;
    private List<PaymentMethod> paymentMethods;
    private List<String> codes;
    private List<String> names;
    private Boolean active;

    @Size(min = 2, max = 2, message = "createdAtRange must contain exactly two dates")
    private List<LocalDateTime> createdAtRange;

    @Size(min = 2, max = 2, message = "updatedAtRange must contain exactly two dates")
    private List<LocalDateTime> updatedAtRange;

    @Min(0)
    private Integer offset;

    @Min(1)
    private Integer limit;

    private String searchPhrase;
    private String sort;
}
