package com.uab.taller.store.service.interfaces;

import com.uab.taller.store.domain.Account;
import java.util.List;
import java.util.Optional;

public interface IAccountService extends IGenericRepository<Account> {

    /**
     * Busca una cuenta por su número de cuenta
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Obtiene todas las cuentas de un usuario específico
     */
    List<Account> findAccountsByUserId(Long userId);

    /**
     * Obtiene cuentas activas de un usuario
     */
    List<Account> findActiveAccountsByUserId(Long userId);

    /**
     * Verifica si una cuenta está activa
     */
    boolean isAccountActive(Long accountId);

    /**
     * Actualiza el saldo de una cuenta
     */
    Account updateBalance(Long accountId, java.math.BigDecimal newBalance);

    /**
     * Genera un número de cuenta único
     */
    String generateAccountNumber();

    /**
     * Obtiene cuentas por tipo
     */
    List<Account> findAccountsByType(String type);

    /**
     * Obtiene cuentas por estado
     */
    List<Account> findAccountsByStatus(String status);
}
