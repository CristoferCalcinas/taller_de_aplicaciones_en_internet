package com.uab.taller.store.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
    private String description;
}
