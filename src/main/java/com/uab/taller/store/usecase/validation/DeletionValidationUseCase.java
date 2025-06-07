package com.uab.taller.store.usecase.validation;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.domain.Rol;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.exception.EntityDeletionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeletionValidationUseCase {

    public void validateUserDeletion(User user) {
        List<String> violations = new ArrayList<>();

        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        // Validar cuentas activas con saldo
        if (user.getAccounts() != null && !user.getAccounts().isEmpty()) {
            long activeAccountsWithBalance = user.getAccounts().stream()
                .filter(account -> "ACTIVE".equals(account.getStatus()))
                .filter(account -> account.getBalance() != null && 
                         account.getBalance().compareTo(BigDecimal.ZERO) > 0)
                .count();
            
            if (activeAccountsWithBalance > 0) {
                violations.add(String.format("El usuario tiene %d cuentas activas con saldo", 
                    activeAccountsWithBalance));
            }
        }

        // Validar transacciones pendientes
        if (user.getAccounts() != null) {
            long accountsWithTransactions = user.getAccounts().stream()
                .filter(account -> 
                    (account.getOutgoingTransactions() != null && !account.getOutgoingTransactions().isEmpty()) ||
                    (account.getIncomingTransactions() != null && !account.getIncomingTransactions().isEmpty()))
                .count();
            
            if (accountsWithTransactions > 0) {
                violations.add(String.format("El usuario tiene cuentas con transacciones asociadas"));
            }
        }

        // Validar beneficiarios activos
        if (user.getBeneficiaries() != null && !user.getBeneficiaries().isEmpty()) {
            long activeBeneficiaries = user.getBeneficiaries().stream()
                .filter(beneficiary -> !beneficiary.isDeleted())
                .count();
            
            if (activeBeneficiaries > 0) {
                violations.add(String.format("El usuario tiene %d beneficiarios activos", 
                    activeBeneficiaries));
            }
        }

        if (!violations.isEmpty()) {
            throw new EntityDeletionException("Usuario", user.getId(), 
                String.join("; ", violations));
        }
    }

    public void validateAccountDeletion(Account account) {
        List<String> violations = new ArrayList<>();

        if (account == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }

        // Validar saldo
        if (account.getBalance() != null && account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            violations.add(String.format("La cuenta tiene un saldo de %s", 
                account.getBalance().toString()));
        }

        // Validar transacciones
        if ((account.getOutgoingTransactions() != null && !account.getOutgoingTransactions().isEmpty()) ||
            (account.getIncomingTransactions() != null && !account.getIncomingTransactions().isEmpty())) {
            violations.add("La cuenta tiene transacciones asociadas");
        }

        // Validar beneficiarios
        if (account.getAccountBeneficiaries() != null && !account.getAccountBeneficiaries().isEmpty()) {
            long activeBeneficiaries = account.getAccountBeneficiaries().stream()
                .filter(beneficiary -> !beneficiary.isDeleted())
                .count();
            
            if (activeBeneficiaries > 0) {
                violations.add(String.format("La cuenta tiene %d beneficiarios activos", 
                    activeBeneficiaries));
            }
        }

        if (!violations.isEmpty()) {
            throw new EntityDeletionException("Cuenta", account.getId(), 
                String.join("; ", violations));
        }
    }

    public void validateAccountStatus(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new EntityDeletionException("Cuenta", account.getId(), 
                "Solo se pueden eliminar cuentas en estado ACTIVE");
        }
    }

    public void validateBeneficiaryDeletion(Beneficiary beneficiary) {
        if (beneficiary == null) {
            throw new IllegalArgumentException("El beneficiario no puede ser nulo");
        }

        // Validar que el beneficiario no esté siendo usado en transacciones pendientes
        // En una implementación real, aquí verificarías si hay transacciones pendientes
        // que involucren a este beneficiario
    }

    public void validateProfileDeletion(Profile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("El perfil no puede ser nulo");
        }

        // Validar que el perfil no esté asociado a usuarios activos
        // En una implementación real, aquí verificarías si hay usuarios con este perfil
    }

    public void validateRolDeletion(Rol rol) {
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }

        // Validar que el rol no esté siendo usado por usuarios activos
        // En una implementación real, aquí verificarías si hay usuarios con este rol
        // También validar que no sea un rol del sistema que no se puede eliminar
        if ("ADMIN".equals(rol.getName()) || "USER".equals(rol.getName())) {
            throw new EntityDeletionException("Rol", rol.getId(), 
                "Los roles del sistema no se pueden eliminar");
        }
    }
}
