package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Caso de uso para operaciones de saldo en cuentas bancarias
 */
@Service
public class BalanceOperationUseCase {

    @Autowired
    private IAccountService accountService;

    /**
     * Actualiza el saldo de una cuenta
     * 
     * @param accountId  ID de la cuenta
     * @param newBalance Nuevo saldo
     * @param reason     Razón del cambio de saldo
     * @return Cuenta actualizada
     */
    @Transactional
    public Account updateBalance(Long accountId, BigDecimal newBalance, String reason) {
        if (accountId == null) {
            throw new IllegalArgumentException("El ID de la cuenta no puede ser nulo");
        }

        if (newBalance == null) {
            throw new IllegalArgumentException("El nuevo saldo no puede ser nulo");
        }

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo");
        }

        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new EntityNotFoundException("Cuenta", accountId);
        }

        // Validar que la cuenta esté activa
        if (!"ACTIVE".equals(account.getStatus())) {
            throw new IllegalStateException("Solo se puede actualizar el saldo de cuentas activas");
        }

        BigDecimal previousBalance = account.getBalance();
        account.setBalance(newBalance);
        account.setChangeDate(LocalDateTime.now());
        account.setChangeUser("SYSTEM_BALANCE_" + (reason != null ? reason : "UPDATE"));

        Account updatedAccount = accountService.update(account);

        // Log del cambio de saldo
        logBalanceChange(account, previousBalance, newBalance, reason);

        return updatedAccount;
    }

    /**
     * Añade una cantidad al saldo actual
     * 
     * @param accountId ID de la cuenta
     * @param amount    Cantidad a añadir
     * @param reason    Razón del incremento
     * @return Cuenta actualizada
     */
    @Transactional
    public Account addToBalance(Long accountId, BigDecimal amount, String reason) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a añadir debe ser positivo");
        }

        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new EntityNotFoundException("Cuenta", accountId);
        }

        BigDecimal newBalance = account.getBalance().add(amount);
        return updateBalance(accountId, newBalance, reason != null ? reason : "ADD");
    }

    /**
     * Resta una cantidad del saldo actual
     * 
     * @param accountId ID de la cuenta
     * @param amount    Cantidad a restar
     * @param reason    Razón de la deducción
     * @return Cuenta actualizada
     */
    @Transactional
    public Account subtractFromBalance(Long accountId, BigDecimal amount, String reason) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a restar debe ser positivo");
        }

        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new EntityNotFoundException("Cuenta", accountId);
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo resultante no puede ser negativo");
        }

        return updateBalance(accountId, newBalance, reason != null ? reason : "SUBTRACT");
    }

    /**
     * Verifica si una cuenta tiene saldo suficiente
     * 
     * @param accountId      ID de la cuenta
     * @param requiredAmount Cantidad requerida
     * @return true si tiene saldo suficiente, false en caso contrario
     */
    public boolean hasSufficientBalance(Long accountId, BigDecimal requiredAmount) {
        if (accountId == null || requiredAmount == null) {
            return false;
        }

        try {
            Account account = accountService.findById(accountId);
            return account != null && account.getBalance().compareTo(requiredAmount) >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene el saldo actual de una cuenta
     * 
     * @param accountId ID de la cuenta
     * @return Saldo actual
     */
    public BigDecimal getCurrentBalance(Long accountId) {
        if (accountId == null) {
            return BigDecimal.ZERO;
        }

        Account account = accountService.findById(accountId);
        return account != null ? account.getBalance() : BigDecimal.ZERO;
    }

    /**
     * Congela temporalmente fondos en una cuenta
     * 
     * @param accountId ID de la cuenta
     * @param amount    Cantidad a congelar
     * @param reason    Razón del congelamiento
     * @return true si se congeló exitosamente
     */
    @Transactional
    public boolean freezeFunds(Long accountId, BigDecimal amount, String reason) {
        if (!hasSufficientBalance(accountId, amount)) {
            return false;
        }

        // En una implementación real, esto requeriría una tabla adicional
        // para manejar fondos congelados. Por ahora, solo validamos.
        return true;
    }

    /**
     * Registra cambios en el saldo para auditoría
     */
    private void logBalanceChange(Account account, BigDecimal previousBalance,
            BigDecimal newBalance, String reason) {
        // En una implementación real, esto se guardaría en una tabla de auditoría
        System.out.printf("BALANCE_CHANGE: Account[%d] %s -> %s, Reason: %s, Time: %s%n",
                account.getId(),
                previousBalance.toString(),
                newBalance.toString(),
                reason != null ? reason : "UNKNOWN",
                LocalDateTime.now());
    }
}
