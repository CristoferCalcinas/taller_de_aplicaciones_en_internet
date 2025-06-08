package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.response.TransactionStatisticsResponse;
import com.uab.taller.store.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Caso de uso para generar estadísticas de transacciones
 */
@Service
public class TransactionStatisticsUseCase {

        @Autowired
        private TransactionRepository transactionRepository;

        /**
         * Obtiene estadísticas de transacciones por cuenta
         */
        public TransactionStatisticsResponse getStatisticsByAccount(Long accountId) {
                List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
                return generateStatistics(transactions, "Cuenta específica");
        }

        /**
         * Obtiene estadísticas de transacciones por rango de fechas
         */
        public TransactionStatisticsResponse getStatisticsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
                List<Transaction> transactions = transactionRepository.findByDateBetween(startDate, endDate);
                return generateStatistics(transactions, "Rango de fechas");
        }

        /**
         * Obtiene estadísticas generales de todas las transacciones
         */
        public TransactionStatisticsResponse getGeneralStatistics() {
                List<Transaction> transactions = transactionRepository.findAll();
                return generateStatistics(transactions, "General");
        }

        /**
         * Genera estadísticas a partir de una lista de transacciones
         */
        private TransactionStatisticsResponse generateStatistics(List<Transaction> transactions, String period) {
                if (transactions.isEmpty()) {
                        return TransactionStatisticsResponse.builder()
                                        .totalTransactions(0)
                                        .totalAmount(BigDecimal.ZERO)
                                        .totalIncome(BigDecimal.ZERO)
                                        .totalExpenses(BigDecimal.ZERO)
                                        .period(period)
                                        .transactionsByType(List.of())
                                        .build();
                }

                // Calcular totales
                BigDecimal totalAmount = transactions.stream()
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalIncome = transactions.stream()
                                .filter(t -> "DEPOSIT".equals(t.getTransactionType()))
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalExpenses = transactions.stream()
                                .filter(t -> "WITHDRAWAL".equals(t.getTransactionType())
                                                || "TRANSFER".equals(t.getTransactionType()))
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Agrupar por tipo
                Map<String, List<Transaction>> groupedByType = transactions.stream()
                                .collect(Collectors.groupingBy(Transaction::getTransactionType));

                List<TransactionStatisticsResponse.TypeStatistic> typeStatistics = groupedByType.entrySet().stream()
                                .map(entry -> {
                                        String type = entry.getKey();
                                        List<Transaction> typeTransactions = entry.getValue();
                                        BigDecimal typeAmount = typeTransactions.stream()
                                                        .map(Transaction::getAmount)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                        double percentage = totalAmount.compareTo(BigDecimal.ZERO) > 0
                                                        ? typeAmount.divide(totalAmount, 4, RoundingMode.HALF_UP)
                                                                        .multiply(new BigDecimal("100"))
                                                                        .doubleValue()
                                                        : 0.0;

                                        return TransactionStatisticsResponse.TypeStatistic.builder()
                                                        .type(type)
                                                        .count(typeTransactions.size())
                                                        .totalAmount(typeAmount)
                                                        .percentage(percentage)
                                                        .build();
                                })
                                .collect(Collectors.toList());

                return TransactionStatisticsResponse.builder()
                                .totalTransactions(transactions.size())
                                .totalAmount(totalAmount)
                                .totalIncome(totalIncome)
                                .totalExpenses(totalExpenses)
                                .transactionsByType(typeStatistics)
                                .period(period)
                                .build();
        }
}
