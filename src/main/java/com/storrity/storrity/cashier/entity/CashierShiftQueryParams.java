package com.storrity.storrity.cashier.entity;

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
public class CashierShiftQueryParams {

    private UUID id;
    private UUID storeId;
    private String cashierUsername;
    private CashierShiftStatus status;

    @Size(min = 2, max = 2, message = "openedAtRange must contain exactly two dates")
    private List<LocalDateTime> openedAtRange;

    @Size(min = 2, max = 2, message = "closedAtRange must contain exactly two dates")
    private List<LocalDateTime> closedAtRange;

    private Integer offset;
    private Integer limit;
    private String searchPhrase;
    private String sort;
}