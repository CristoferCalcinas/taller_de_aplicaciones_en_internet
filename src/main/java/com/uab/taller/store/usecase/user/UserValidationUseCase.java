package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserValidationUseCase {

    @Autowired
    private IUserService userService;

    /**
     * Valida si un usuario puede ser eliminado
     */
    public void validateUserDeletion(User user) {
        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        List<String> violations = new ArrayList<>();

        // Verificar si tiene cuentas activas con saldo
        if (user.getAccounts() != null) {
            long activeAccountsWithBalance = user.getAccounts().stream()
                    .filter(account -> !account.isDeleted())
                    .filter(account -> account.getBalance() != null && account.getBalance().doubleValue() > 0)
                    .count();

            if (activeAccountsWithBalance > 0) {
                violations.add("El usuario tiene " + activeAccountsWithBalance + " cuenta(s) con saldo positivo");
            }
        }

        // Verificar si tiene beneficiarios activos
        if (user.getBeneficiaries() != null) {
            long activeBeneficiaries = user.getBeneficiaries().stream()
                    .filter(beneficiary -> !beneficiary.isDeleted())
                    .count();

            if (activeBeneficiaries > 0) {
                violations.add("El usuario tiene " + activeBeneficiaries + " beneficiario(s) activo(s)");
            }
        }

        if (!violations.isEmpty()) {
            throw new EntityDeletionException("Usuario", user.getId(),
                    String.join("; ", violations));
        }
    }

    /**
     * Valida si un email está disponible para uso
     */
    public boolean isEmailAvailable(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        Optional<User> existingUser = userService.getByEmail(email.trim());
        return existingUser.isEmpty();
    }

    /**
     * Valida si un email está disponible para un usuario específico (para
     * actualizaciones)
     */
    public boolean isEmailAvailableForUser(String email, Long userId) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        Optional<User> existingUser = userService.getByEmail(email.trim());
        return existingUser.isEmpty() || existingUser.get().getId().equals(userId);
    }

    /**
     * Valida la fortaleza de una contraseña
     */
    public List<String> validatePasswordStrength(String password) {
        List<String> violations = new ArrayList<>();

        if (password == null || password.length() < 8) {
            violations.add("La contraseña debe tener al menos 8 caracteres");
        }

        if (password != null) {
            if (!password.matches(".*[a-z].*")) {
                violations.add("La contraseña debe contener al menos una letra minúscula");
            }

            if (!password.matches(".*[A-Z].*")) {
                violations.add("La contraseña debe contener al menos una letra mayúscula");
            }

            if (!password.matches(".*\\d.*")) {
                violations.add("La contraseña debe contener al menos un número");
            }

            if (password.length() > 255) {
                violations.add("La contraseña no puede exceder 255 caracteres");
            }
        }

        return violations;
    }

    /**
     * Valida si un usuario puede iniciar sesión
     */
    public void validateUserCanLogin(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        if (user.isDeleted()) {
            throw new IllegalStateException("La cuenta está desactivada");
        }

        if (user.getProfile() != null) {
            String status = user.getProfile().getStatus();
            if ("INACTIVE".equals(status)) {
                throw new IllegalStateException("La cuenta está inactiva");
            } else if ("SUSPENDED".equals(status)) {
                throw new IllegalStateException("La cuenta está suspendida");
            } else if ("PENDING".equals(status)) {
                throw new IllegalStateException("La cuenta está pendiente de activación");
            }
        }
    }
}
