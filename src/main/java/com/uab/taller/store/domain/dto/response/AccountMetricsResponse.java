package com.uab.taller.store.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountMetricsResponse {

    private Long accountId;
    private String accountNumber;
    private BigDecimal currentBalance;
    private Long accountAge; // días desde creación
    private TransactionMetrics transactionMetrics;
    private List<MonthlyMetric> monthlyMetrics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionMetrics {
        private Integer totalTransactions;
        private BigDecimal totalIncoming;
        private BigDecimal totalOutgoing;
        private BigDecimal averageTransactionAmount;
        private BigDecimal largestTransaction;
        private BigDecimal smallestTransaction;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lastTransactionDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyMetric {
        private String month; // formato YYYY-MM
        private Integer transactionCount;
        private BigDecimal totalIncoming;
        private BigDecimal totalOutgoing;
        private BigDecimal netAmount; // incoming - outgoing
    }
}
