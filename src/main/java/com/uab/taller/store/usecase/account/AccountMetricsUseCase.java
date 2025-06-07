package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.response.AccountMetricsResponse;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Caso de uso para generar métricas y reportes de cuentas
 */
@Service
public class AccountMetricsUseCase {

    @Autowired
    private IAccountService accountService;

    /**
     * Obtiene métricas completas de una cuenta
     */
    public AccountMetricsResponse getAccountMetrics(Long accountId) {
        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        return AccountMetricsResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .currentBalance(account.getBalance())
                .accountAge(calculateAccountAge(account))
                .transactionMetrics(calculateTransactionMetrics(account))
                .monthlyMetrics(calculateMonthlyMetrics(account))
                .build();
    }

    /**
     * Obtiene métricas de todas las cuentas de un usuario
     */
    public List<AccountMetricsResponse> getUserAccountsMetrics(Long userId) {
        List<Account> accounts = accountService.findAccountsByUserId(userId);
        return accounts.stream()
                .map(account -> getAccountMetrics(account.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Calcula la edad de la cuenta en días
     */
    private long calculateAccountAge(Account account) {
        if (account.getAddDate() == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(account.getAddDate(), LocalDateTime.now());
    }

    /**
     * Calcula métricas de transacciones
     */
    private AccountMetricsResponse.TransactionMetrics calculateTransactionMetrics(Account account) {
        List<Transaction> allTransactions = getAllTransactions(account);

        if (allTransactions.isEmpty()) {
            return AccountMetricsResponse.TransactionMetrics.builder()
                    .totalTransactions(0)
                    .totalIncoming(BigDecimal.ZERO)
                    .totalOutgoing(BigDecimal.ZERO)
                    .averageTransactionAmount(BigDecimal.ZERO)
                    .largestTransaction(BigDecimal.ZERO)
                    .smallestTransaction(BigDecimal.ZERO)
                    .lastTransactionDate(null)
                    .build();
        }

        BigDecimal totalIncoming = account.getIncomingTransactions().stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOutgoing = account.getOutgoingTransactions().stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = totalIncoming.add(totalOutgoing);
        BigDecimal averageAmount = totalAmount.divide(
                BigDecimal.valueOf(allTransactions.size()), 2, RoundingMode.HALF_UP);

        BigDecimal largestTransaction = allTransactions.stream()
                .map(Transaction::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal smallestTransaction = allTransactions.stream()
                .map(Transaction::getAmount)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        LocalDateTime lastTransactionDate = allTransactions.stream()
                .map(Transaction::getDate)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return AccountMetricsResponse.TransactionMetrics.builder()
                .totalTransactions(allTransactions.size())
                .totalIncoming(totalIncoming)
                .totalOutgoing(totalOutgoing)
                .averageTransactionAmount(averageAmount)
                .largestTransaction(largestTransaction)
                .smallestTransaction(smallestTransaction)
                .lastTransactionDate(lastTransactionDate)
                .build();
    }

    /**
     * Calcula métricas mensuales
     */
    private List<AccountMetricsResponse.MonthlyMetric> calculateMonthlyMetrics(Account account) {
        List<Transaction> allTransactions = getAllTransactions(account);

        Map<String, List<Transaction>> transactionsByMonth = allTransactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getDate().getYear() + "-" + String.format("%02d", t.getDate().getMonthValue())));

        return transactionsByMonth.entrySet().stream()
                .map(entry -> {
                    String month = entry.getKey();
                    List<Transaction> monthTransactions = entry.getValue();

                    BigDecimal monthlyIncoming = monthTransactions.stream()
                            .filter(t -> account.getIncomingTransactions().contains(t))
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal monthlyOutgoing = monthTransactions.stream()
                            .filter(t -> account.getOutgoingTransactions().contains(t))
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return AccountMetricsResponse.MonthlyMetric.builder()
                            .month(month)
                            .transactionCount(monthTransactions.size())
                            .totalIncoming(monthlyIncoming)
                            .totalOutgoing(monthlyOutgoing)
                            .netAmount(monthlyIncoming.subtract(monthlyOutgoing))
                            .build();
                })
                .sorted((m1, m2) -> m2.getMonth().compareTo(m1.getMonth())) // Más recientes primero
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las transacciones de una cuenta
     */
    private List<Transaction> getAllTransactions(Account account) {
        List<Transaction> allTransactions = account.getIncomingTransactions().stream().collect(Collectors.toList());
        allTransactions.addAll(account.getOutgoingTransactions());
        return allTransactions;
    }

    /**
     * Calcula el promedio de saldo en un período
     */
    public BigDecimal calculateAverageBalance(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        // En una implementación real, esto requeriría un historial de saldos
        // Por ahora, devolvemos el saldo actual
        Account account = accountService.findById(accountId);
        return account != null ? account.getBalance() : BigDecimal.ZERO;
    }

    /**
     * Obtiene la actividad de la cuenta (transacciones por día)
     */
    public Map<String, Integer> getAccountActivity(Long accountId, int days) {
        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<Transaction> recentTransactions = getAllTransactions(account).stream()
                .filter(t -> t.getDate().isAfter(startDate))
                .collect(Collectors.toList());

        return recentTransactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getDate().toLocalDate().toString(),
                        Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)));
    }
}
