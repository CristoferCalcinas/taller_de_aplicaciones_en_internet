package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Caso de uso para validaciones de cuentas bancarias
 */
@Service
public class AccountValidationUseCase {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserService userService;

    /**
     * Valida si una cuenta puede ser creada para un usuario
     */
    public ValidationResult validateAccountCreation(Long userId, String accountType, String currency) {
        List<String> errors = new ArrayList<>();

        // Validar usuario
        if (userId == null) {
            errors.add("El ID del usuario es obligatorio");
        } else {
            try {
                User user = userService.findById(userId);
                if (user == null) {
                    errors.add("El usuario no existe");
                } else if (user.isDeleted()) {
                    errors.add("No se puede crear una cuenta para un usuario eliminado");
                }
            } catch (Exception e) {
                errors.add("Error al validar el usuario: " + e.getMessage());
            }
        }

        // Validar tipo de cuenta
        if (accountType == null || accountType.trim().isEmpty()) {
            errors.add("El tipo de cuenta es obligatorio");
        } else if (!isValidAccountType(accountType)) {
            errors.add("Tipo de cuenta no válido: " + accountType);
        }

        // Validar moneda
        if (currency == null || currency.trim().isEmpty()) {
            errors.add("La moneda es obligatoria");
        } else if (!isValidCurrency(currency)) {
            errors.add("Moneda no válida: " + currency);
        }

        // Validar límite de cuentas por usuario
        if (userId != null) {
            List<Account> userAccounts = accountService.findActiveAccountsByUserId(userId);
            if (userAccounts.size() >= getMaxAccountsPerUser()) {
                errors.add("El usuario ha alcanzado el límite máximo de cuentas (" + getMaxAccountsPerUser() + ")");
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valida si un número de cuenta es único
     */
    public ValidationResult validateUniqueAccountNumber(String accountNumber) {
        List<String> errors = new ArrayList<>();

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            errors.add("El número de cuenta no puede estar vacío");
        } else {
            Optional<Account> existingAccount = accountService.findByAccountNumber(accountNumber);
            if (existingAccount.isPresent()) {
                errors.add("El número de cuenta ya existe: " + accountNumber);
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valida si una cuenta puede ser actualizada
     */
    public ValidationResult validateAccountUpdate(Long accountId, String newStatus, BigDecimal newBalance) {
        List<String> errors = new ArrayList<>();

        // Validar que la cuenta existe
        if (accountId == null) {
            errors.add("El ID de la cuenta es obligatorio");
            return new ValidationResult(false, errors);
        }

        Account account = accountService.findById(accountId);
        if (account == null) {
            errors.add("La cuenta no existe");
            return new ValidationResult(false, errors);
        }

        // Validar estado actual
        if (account.isDeleted()) {
            errors.add("No se puede actualizar una cuenta eliminada");
        }

        // Validar transición de estado
        if (newStatus != null && !newStatus.equals(account.getStatus())) {
            ValidationResult statusValidation = validateStatusTransition(account.getStatus(), newStatus);
            if (!statusValidation.isValid()) {
                errors.addAll(statusValidation.getErrors());
            }
        }

        // Validar nuevo saldo
        if (newBalance != null) {
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                errors.add("El saldo no puede ser negativo");
            }

            if ("CLOSED".equals(account.getStatus()) && newBalance.compareTo(BigDecimal.ZERO) > 0) {
                errors.add("Una cuenta cerrada debe tener saldo cero");
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valida si una cuenta puede ser eliminada
     */
    public ValidationResult validateAccountDeletion(Long accountId) {
        List<String> errors = new ArrayList<>();

        if (accountId == null) {
            errors.add("El ID de la cuenta es obligatorio");
            return new ValidationResult(false, errors);
        }

        Account account = accountService.findById(accountId);
        if (account == null) {
            errors.add("La cuenta no existe");
            return new ValidationResult(false, errors);
        }

        // Validar saldo
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            errors.add("No se puede eliminar una cuenta con saldo positivo");
        }

        // Validar transacciones pendientes
        boolean hasTransactions = (account.getOutgoingTransactions() != null
                && !account.getOutgoingTransactions().isEmpty()) ||
                (account.getIncomingTransactions() != null && !account.getIncomingTransactions().isEmpty());

        if (hasTransactions) {
            errors.add("No se puede eliminar una cuenta con transacciones asociadas");
        }

        // Validar beneficiarios activos
        if (account.getAccountBeneficiaries() != null) {
            long activeBeneficiaries = account.getAccountBeneficiaries().stream()
                    .filter(b -> !b.isDeleted())
                    .count();

            if (activeBeneficiaries > 0) {
                errors.add("No se puede eliminar una cuenta con beneficiarios activos");
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valida transiciones de estado de cuenta
     */
    public ValidationResult validateStatusTransition(String currentStatus, String newStatus) {
        List<String> errors = new ArrayList<>();

        if (currentStatus == null || newStatus == null) {
            errors.add("Los estados no pueden ser nulos");
            return new ValidationResult(false, errors);
        }

        if (!isValidAccountStatus(newStatus)) {
            errors.add("Estado no válido: " + newStatus);
            return new ValidationResult(false, errors);
        }

        // Reglas de transición
        switch (currentStatus) {
            case "ACTIVE":
                if (!newStatus.equals("SUSPENDED") && !newStatus.equals("CLOSED")) {
                    errors.add("Desde ACTIVE solo se puede ir a SUSPENDED o CLOSED");
                }
                break;
            case "SUSPENDED":
                if (!newStatus.equals("ACTIVE") && !newStatus.equals("CLOSED")) {
                    errors.add("Desde SUSPENDED solo se puede ir a ACTIVE o CLOSED");
                }
                break;
            case "CLOSED":
                errors.add("No se puede cambiar el estado de una cuenta cerrada");
                break;
            default:
                errors.add("Estado actual no reconocido: " + currentStatus);
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    // Métodos auxiliares
    private boolean isValidAccountType(String type) {
        return type != null && ("SAVINGS".equalsIgnoreCase(type) ||
                "CHECKING".equalsIgnoreCase(type) ||
                "BUSINESS".equalsIgnoreCase(type) ||
                "INVESTMENT".equalsIgnoreCase(type));
    }

    private boolean isValidCurrency(String currency) {
        return currency != null && ("USD".equalsIgnoreCase(currency) ||
                "EUR".equalsIgnoreCase(currency) ||
                "GBP".equalsIgnoreCase(currency) ||
                "CAD".equalsIgnoreCase(currency) ||
                "AUD".equalsIgnoreCase(currency));
    }

    private boolean isValidAccountStatus(String status) {
        return status != null && ("ACTIVE".equals(status) ||
                "SUSPENDED".equals(status) ||
                "CLOSED".equals(status));
    }

    private int getMaxAccountsPerUser() {
        return 5; // Límite configurable
    }

    /**
     * Clase para encapsular resultados de validación
     */
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;

        public ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors != null ? errors : new ArrayList<>();
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return new ArrayList<>(errors);
        }

        public String getErrorMessage() {
            return String.join("; ", errors);
        }
    }
}
