package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Caso de uso para congelar/suspender una cuenta bancaria
 */
@Service
public class FreezeAccountUseCase {

    @Autowired
    private IAccountService accountService;

    /**
     * Congela una cuenta cambiando su estado a SUSPENDED
     * 
     * @param accountId ID de la cuenta a congelar
     * @return Cuenta actualizada
     * @throws EntityNotFoundException si la cuenta no existe
     * @throws IllegalStateException   si la cuenta ya está congelada o cerrada
     */
    @Transactional
    public Account freezeAccount(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("El ID de la cuenta no puede ser nulo");
        }

        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new EntityNotFoundException("Cuenta", accountId);
        }

        // Validar estado actual
        if ("SUSPENDED".equals(account.getStatus())) {
            throw new IllegalStateException("La cuenta ya está suspendida");
        }

        if ("CLOSED".equals(account.getStatus())) {
            throw new IllegalStateException("No se puede suspender una cuenta cerrada");
        }

        // Cambiar estado a suspendido
        account.setStatus("SUSPENDED");
        account.setChangeDate(LocalDateTime.now());
        account.setChangeUser("SYSTEM_FREEZE");

        return accountService.update(account);
    }

    /**
     * Descongelar una cuenta cambiando su estado a ACTIVE
     * 
     * @param accountId ID de la cuenta a descongelar
     * @return Cuenta actualizada
     * @throws EntityNotFoundException si la cuenta no existe
     * @throws IllegalStateException   si la cuenta no está congelada
     */
    @Transactional
    public Account unfreezeAccount(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("El ID de la cuenta no puede ser nulo");
        }

        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new EntityNotFoundException("Cuenta", accountId);
        }

        // Validar estado actual
        if (!"SUSPENDED".equals(account.getStatus())) {
            throw new IllegalStateException("Solo se pueden descongelar cuentas suspendidas");
        }

        // Cambiar estado a activo
        account.setStatus("ACTIVE");
        account.setChangeDate(LocalDateTime.now());
        account.setChangeUser("SYSTEM_UNFREEZE");

        return accountService.update(account);
    }

    /**
     * Verifica si una cuenta está congelada
     * 
     * @param accountId ID de la cuenta
     * @return true si está congelada, false en caso contrario
     */
    public boolean isAccountFrozen(Long accountId) {
        if (accountId == null) {
            return false;
        }

        try {
            Account account = accountService.findById(accountId);
            return account != null && "SUSPENDED".equals(account.getStatus());
        } catch (Exception e) {
            return false;
        }
    }
}
