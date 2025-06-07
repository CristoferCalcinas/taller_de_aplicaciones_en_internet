package com.uab.taller.store.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountSummaryResponse {

    private Long id;
    private String accountNumber;
    private String type;
    private String currency;
    private BigDecimal balance;
    private String status;

    // Resumen de transacciones recientes (últimas 5)
    private List<TransactionSummary> recentTransactions;

    // Estadísticas del mes actual
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private Integer transactionCount;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TransactionSummary {
        private Long id;
        private String type;
        private BigDecimal amount;
        private String description;
        private String date;
        private String targetAccountNumber;
    }
}
