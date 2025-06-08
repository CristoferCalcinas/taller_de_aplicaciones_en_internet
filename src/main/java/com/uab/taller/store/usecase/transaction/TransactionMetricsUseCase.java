package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.response.TransactionMetricsResponse;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Caso de uso para generar métricas y dashboard ejecutivo
 */
@Service
public class TransactionMetricsUseCase {

        @Autowired
        private ITransactionService transactionService;

        /**
         * Genera métricas completas del sistema
         */
        public TransactionMetricsResponse getSystemMetrics() {
                List<Transaction> allTransactions = transactionService.findAll();
                LocalDateTime now = LocalDateTime.now();

                return TransactionMetricsResponse.builder()
                                .systemSummary(buildSystemSummary(allTransactions, now))
                                .transactionTypeMetrics(buildTransactionTypeMetrics(allTransactions))
                                .temporalTrends(buildTemporalTrends(allTransactions))
                                .performanceMetrics(buildPerformanceMetrics(allTransactions))
                                .alerts(buildSystemAlerts(allTransactions))
                                .build();
        }

        /**
         * Construye el resumen general del sistema
         */
        private TransactionMetricsResponse.SystemSummary buildSystemSummary(
                        List<Transaction> transactions, LocalDateTime now) {

                BigDecimal totalVolume = transactions.stream()
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                LocalDate today = now.toLocalDate();
                List<Transaction> dailyTransactions = transactions.stream()
                                .filter(t -> t.getDate().toLocalDate().equals(today))
                                .collect(Collectors.toList());

                BigDecimal dailyVolume = dailyTransactions.stream()
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                long successfulTransactions = transactions.stream()
                                .filter(t -> "COMPLETED".equals(t.getStatus()))
                                .count();

                double successRate = transactions.isEmpty() ? 0.0
                                : (double) successfulTransactions / transactions.size() * 100;

                // Simular conteo de cuentas activas
                long activeAccounts = Math.max(50L, transactions.size() / 10);

                return TransactionMetricsResponse.SystemSummary.builder()
                                .totalTransactions((long) transactions.size())
                                .totalVolume(totalVolume)
                                .activeAccounts(activeAccounts)
                                .dailyTransactions((long) dailyTransactions.size())
                                .dailyVolume(dailyVolume)
                                .generatedAt(now)
                                .successRate(Math.round(successRate * 100.0) / 100.0)
                                .build();
        }

        /**
         * Construye métricas por tipo de transacción
         */
        private List<TransactionMetricsResponse.TransactionTypeMetrics> buildTransactionTypeMetrics(
                        List<Transaction> transactions) {

                Map<String, List<Transaction>> groupedByType = transactions.stream()
                                .collect(Collectors.groupingBy(Transaction::getTransactionType));

                long totalTransactions = transactions.size();

                return groupedByType.entrySet().stream()
                                .map(entry -> {
                                        String type = entry.getKey();
                                        List<Transaction> typeTransactions = entry.getValue();

                                        BigDecimal totalAmount = typeTransactions.stream()
                                                        .map(Transaction::getAmount)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                        BigDecimal averageAmount = typeTransactions.isEmpty() ? BigDecimal.ZERO
                                                        : totalAmount.divide(
                                                                        BigDecimal.valueOf(typeTransactions.size()), 2,
                                                                        RoundingMode.HALF_UP);

                                        double percentage = totalTransactions == 0 ? 0.0
                                                        : (double) typeTransactions.size() / totalTransactions * 100;

                                        // Simular tasa de crecimiento mensual
                                        double growthRate = Math.random() * 20 - 5; // Entre -5% y 15%

                                        return TransactionMetricsResponse.TransactionTypeMetrics.builder()
                                                        .transactionType(type)
                                                        .count((long) typeTransactions.size())
                                                        .totalAmount(totalAmount)
                                                        .averageAmount(averageAmount)
                                                        .percentage(Math.round(percentage * 100.0) / 100.0)
                                                        .monthlyGrowthRate(Math.round(growthRate * 100.0) / 100.0)
                                                        .build();
                                })
                                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                                .collect(Collectors.toList());
        }

        /**
         * Construye tendencias temporales
         */
        private TransactionMetricsResponse.TemporalTrends buildTemporalTrends(
                        List<Transaction> transactions) {

                // Distribución por hora
                Map<Integer, Long> hourlyDistribution = transactions.stream()
                                .collect(Collectors.groupingBy(
                                                t -> t.getDate().getHour(),
                                                Collectors.counting()));

                // Distribución por día de la semana
                Map<String, Long> weeklyDistribution = transactions.stream()
                                .collect(Collectors.groupingBy(
                                                t -> t.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL,
                                                                new Locale("es")),
                                                Collectors.counting()));

                // Volumen por mes
                Map<String, BigDecimal> monthlyVolume = transactions.stream()
                                .collect(Collectors.groupingBy(
                                                t -> t.getDate().getYear() + "-"
                                                                + String.format("%02d", t.getDate().getMonthValue()),
                                                Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount,
                                                                BigDecimal::add)));

                // Encontrar picos
                Integer peakHour = hourlyDistribution.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse(12);

                String peakDay = weeklyDistribution.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse("Lunes");

                return TransactionMetricsResponse.TemporalTrends.builder()
                                .hourlyDistribution(hourlyDistribution)
                                .weeklyDistribution(weeklyDistribution)
                                .monthlyVolume(monthlyVolume)
                                .peakHour(peakHour)
                                .peakDay(peakDay.toUpperCase())
                                .build();
        }

        /**
         * Construye métricas de rendimiento (simuladas)
         */
        private TransactionMetricsResponse.PerformanceMetrics buildPerformanceMetrics(
                        List<Transaction> transactions) {

                return TransactionMetricsResponse.PerformanceMetrics.builder()
                                .averageProcessingTime(125.5 + Math.random() * 50) // Simular entre 125-175ms
                                .transactionsPerSecond(45.2 + Math.random() * 10) // Simular entre 45-55 TPS
                                .p95ResponseTime(250.0 + Math.random() * 100) // Simular entre 250-350ms
                                .systemUptime(99.8 + Math.random() * 0.2) // Simular entre 99.8-100%
                                .failedTransactions24h((long) (Math.random() * 20)) // Simular 0-20 fallos
                                .build();
        }

        /**
         * Construye alertas del sistema
         */
        private List<TransactionMetricsResponse.SystemAlert> buildSystemAlerts(
                        List<Transaction> transactions) {

                List<TransactionMetricsResponse.SystemAlert> alerts = new ArrayList<>();
                LocalDateTime now = LocalDateTime.now();

                // Verificar volumen inusual
                LocalDate today = now.toLocalDate();
                long dailyTransactions = transactions.stream()
                                .filter(t -> t.getDate().toLocalDate().equals(today))
                                .count();

                if (dailyTransactions > 1000) {
                        alerts.add(TransactionMetricsResponse.SystemAlert.builder()
                                        .alertType("INFO")
                                        .message("Volumen de transacciones diarias por encima del promedio")
                                        .severity("LOW")
                                        .timestamp(now)
                                        .requiresAction(false)
                                        .build());
                }

                // Verificar transacciones fallidas
                long failedTransactions = transactions.stream()
                                .filter(t -> !"COMPLETED".equals(t.getStatus()))
                                .count();

                if (failedTransactions > 10) {
                        alerts.add(TransactionMetricsResponse.SystemAlert.builder()
                                        .alertType("WARNING")
                                        .message("Número elevado de transacciones fallidas detectado")
                                        .severity("MEDIUM")
                                        .timestamp(now)
                                        .requiresAction(true)
                                        .build());
                }

                // Alerta de mantenimiento programado (ejemplo)
                if (now.getHour() == 2) {
                        alerts.add(TransactionMetricsResponse.SystemAlert.builder()
                                        .alertType("MAINTENANCE")
                                        .message("Mantenimiento programado del sistema en 2 horas")
                                        .severity("LOW")
                                        .timestamp(now)
                                        .requiresAction(false)
                                        .build());
                }

                return alerts;
        }
}
