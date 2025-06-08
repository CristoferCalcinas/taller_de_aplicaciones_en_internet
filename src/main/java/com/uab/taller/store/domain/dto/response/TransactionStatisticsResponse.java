package com.uab.taller.store.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatisticsResponse {
    private Integer totalTransactions;
    private BigDecimal totalAmount;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private List<TypeStatistic> transactionsByType;
    private String period;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TypeStatistic {
        private String type;
        private Integer count;
        private BigDecimal totalAmount;
        private Double percentage;
    }
}
