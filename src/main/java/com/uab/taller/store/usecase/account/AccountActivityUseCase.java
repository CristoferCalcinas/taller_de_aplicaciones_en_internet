package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.dto.response.AccountActivityResponse;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso para registrar y obtener actividad de cuentas
 */
@Service
public class AccountActivityUseCase {

    @Autowired
    private IAccountService accountService;

    /**
     * Registra una actividad en la cuenta
     */
    public void logAccountActivity(Long accountId, String activityType, String description, String userAgent) {
        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        AccountActivityLog log = AccountActivityLog.builder()
                .accountId(accountId)
                .activityType(activityType)
                .description(description)
                .userAgent(userAgent)
                .timestamp(LocalDateTime.now())
                .build();

        // En una implementación real, esto se guardaría en una tabla de logs
        saveActivityLog(log);
    }

    /**
     * Obtiene el historial de actividades de una cuenta
     */
    public AccountActivityResponse getAccountActivity(Long accountId, int limit) {
        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        List<AccountActivityLog> activities = getActivityLogs(accountId, limit);

        return AccountActivityResponse.builder()
                .accountId(accountId)
                .accountNumber(account.getAccountNumber())
                .totalActivities(activities.size())
                .activities(activities.stream()
                        .map(this::convertToActivityItem)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Registra acceso a la cuenta
     */
    public void logAccountAccess(Long accountId, String userAgent, String ipAddress) {
        logAccountActivity(accountId, "ACCESS",
                "Acceso a cuenta desde " + ipAddress, userAgent);
    }

    /**
     * Registra modificación de cuenta
     */
    public void logAccountModification(Long accountId, String fieldChanged, String oldValue, String newValue) {
        String description = String.format("Campo '%s' cambiado de '%s' a '%s'",
                fieldChanged, oldValue, newValue);
        logAccountActivity(accountId, "MODIFICATION", description, "SYSTEM");
    }

    /**
     * Registra cambio de estado
     */
    public void logStatusChange(Long accountId, String oldStatus, String newStatus, String reason) {
        String description = String.format("Estado cambiado de '%s' a '%s'. Razón: %s",
                oldStatus, newStatus, reason != null ? reason : "No especificada");
        logAccountActivity(accountId, "STATUS_CHANGE", description, "SYSTEM");
    }

    /**
     * Registra operación de saldo
     */
    public void logBalanceOperation(Long accountId, String operationType, String amount, String reason) {
        String description = String.format("Operación de saldo: %s por %s. Razón: %s",
                operationType, amount, reason != null ? reason : "No especificada");
        logAccountActivity(accountId, "BALANCE_OPERATION", description, "SYSTEM");
    }

    /**
     * Obtiene estadísticas de actividad
     */
    public AccountActivityResponse.ActivityStats getActivityStats(Long accountId, int days) {
        List<AccountActivityLog> recentLogs = getRecentActivityLogs(accountId, days);

        long accessCount = recentLogs.stream()
                .filter(log -> "ACCESS".equals(log.getActivityType()))
                .count();

        long modificationCount = recentLogs.stream()
                .filter(log -> "MODIFICATION".equals(log.getActivityType()))
                .count();

        long balanceOperations = recentLogs.stream()
                .filter(log -> "BALANCE_OPERATION".equals(log.getActivityType()))
                .count();

        return AccountActivityResponse.ActivityStats.builder()
                .period(days + " días")
                .totalActivities(recentLogs.size())
                .accessCount((int) accessCount)
                .modificationCount((int) modificationCount)
                .balanceOperations((int) balanceOperations)
                .build();
    }

    // Métodos auxiliares (simulan acceso a base de datos)
    private void saveActivityLog(AccountActivityLog log) {
        // En una implementación real, esto se guardaría en una tabla de logs
        System.out.printf("ACTIVITY_LOG: %s%n", log.toString());
    }

    private List<AccountActivityLog> getActivityLogs(Long accountId, int limit) {
        // Simulación de datos - en una implementación real sería una consulta a BD
        return generateMockActivityLogs(accountId, limit);
    }

    private List<AccountActivityLog> getRecentActivityLogs(Long accountId, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return getActivityLogs(accountId, 100).stream()
                .filter(log -> log.getTimestamp().isAfter(startDate))
                .collect(Collectors.toList());
    }

    private AccountActivityResponse.ActivityItem convertToActivityItem(AccountActivityLog log) {
        return AccountActivityResponse.ActivityItem.builder()
                .activityType(log.getActivityType())
                .description(log.getDescription())
                .timestamp(log.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .userAgent(log.getUserAgent())
                .build();
    }

    private List<AccountActivityLog> generateMockActivityLogs(Long accountId, int limit) {
        List<AccountActivityLog> logs = new ArrayList<>();

        for (int i = 0; i < Math.min(limit, 10); i++) {
            logs.add(AccountActivityLog.builder()
                    .accountId(accountId)
                    .activityType("ACCESS")
                    .description("Acceso a cuenta desde 192.168.1." + (i + 1))
                    .userAgent("Mozilla/5.0")
                    .timestamp(LocalDateTime.now().minusHours(i))
                    .build());
        }

        return logs;
    }

    // Clase interna para el log de actividad
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    private static class AccountActivityLog {
        private Long accountId;
        private String activityType;
        private String description;
        private String userAgent;
        private LocalDateTime timestamp;
    }
}
