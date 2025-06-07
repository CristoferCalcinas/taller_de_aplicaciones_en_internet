package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.dto.response.AccountSummaryResponse;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAccountSummaryUseCase {

    @Autowired
    private IAccountService accountService;

    public AccountSummaryResponse getAccountSummary(Long accountId) {
        Account account = accountService.findById(accountId);

        // Obtener transacciones recientes (últimas 5)
        List<AccountSummaryResponse.TransactionSummary> recentTransactions = getRecentTransactions(account).stream()
                .limit(5)
                .collect(Collectors.toList());

        // Calcular estadísticas del mes actual
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        BigDecimal monthlyIncome = calculateMonthlyIncome(account, startOfMonth);
        BigDecimal monthlyExpenses = calculateMonthlyExpenses(account, startOfMonth);
        int transactionCount = calculateTransactionCount(account, startOfMonth);

        return AccountSummaryResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .type(account.getType())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .status(account.getStatus())
                .recentTransactions(recentTransactions)
                .monthlyIncome(monthlyIncome)
                .monthlyExpenses(monthlyExpenses)
                .transactionCount(transactionCount)
                .build();
    }

    private List<AccountSummaryResponse.TransactionSummary> getRecentTransactions(Account account) {
        // Combinar transacciones entrantes y salientes, ordenar por fecha
        List<AccountSummaryResponse.TransactionSummary> transactions = account.getOutgoingTransactions().stream()
                .map(t -> AccountSummaryResponse.TransactionSummary.builder()
                        .id(t.getId())
                        .type("OUTGOING")
                        .amount(t.getAmount().negate()) // Negativo para salientes
                        .description("Transferencia enviada")
                        .date(t.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .targetAccountNumber(
                                t.getTargetAccount() != null ? t.getTargetAccount().getAccountNumber() : "N/A")
                        .build())
                .collect(Collectors.toList());

        account.getIncomingTransactions().stream()
                .map(t -> AccountSummaryResponse.TransactionSummary.builder()
                        .id(t.getId())
                        .type("INCOMING")
                        .amount(t.getAmount()) // Positivo para entrantes
                        .description("Transferencia recibida")
                        .date(t.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .targetAccountNumber(
                                t.getSourceAccount() != null ? t.getSourceAccount().getAccountNumber() : "N/A")
                        .build())
                .forEach(transactions::add);

        return transactions.stream()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate())) // Más recientes primero
                .collect(Collectors.toList());
    }

    private BigDecimal calculateMonthlyIncome(Account account, LocalDateTime startOfMonth) {
        return account.getIncomingTransactions().stream()
                .filter(t -> t.getDate().isAfter(startOfMonth))
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMonthlyExpenses(Account account, LocalDateTime startOfMonth) {
        return account.getOutgoingTransactions().stream()
                .filter(t -> t.getDate().isAfter(startOfMonth))
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int calculateTransactionCount(Account account, LocalDateTime startOfMonth) {
        int outgoing = (int) account.getOutgoingTransactions().stream()
                .filter(t -> t.getDate().isAfter(startOfMonth))
                .count();

        int incoming = (int) account.getIncomingTransactions().stream()
                .filter(t -> t.getDate().isAfter(startOfMonth))
                .count();

        return outgoing + incoming;
    }
}
