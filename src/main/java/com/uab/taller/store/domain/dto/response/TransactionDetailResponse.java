package com.uab.taller.store.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailResponse {
    private Long id;
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private String status;
    private String currency;

    // Información de cuenta origen
    private AccountInfo sourceAccount;

    // Información de cuenta destino (opcional)
    private AccountInfo targetAccount;

    // Metadatos adicionales
    private String reference;
    private String notes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountInfo {
        private Long id;
        private String accountNumber;
        private String type;
        private String ownerName;
    }
}
