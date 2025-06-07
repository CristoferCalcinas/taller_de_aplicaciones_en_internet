package com.uab.taller.store.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponse {
    private Long id;
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime date;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private String description;
    private String status;
}
