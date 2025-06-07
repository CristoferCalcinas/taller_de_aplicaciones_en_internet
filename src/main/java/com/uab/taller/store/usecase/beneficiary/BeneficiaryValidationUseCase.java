package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.repository.BeneficiaryRepository;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BeneficiaryValidationUseCase {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    /**
     * Valida si se puede crear un beneficiario
     */
    public ValidationResult validateBeneficiaryCreation(Long userId, Long accountId) {
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
                    errors.add("No se puede crear un beneficiario para un usuario eliminado");
                }
            } catch (EntityNotFoundException e) {
                errors.add("El usuario no existe");
            }
        }

        // Validar cuenta
        if (accountId == null) {
            errors.add("El ID de la cuenta es obligatorio");
        } else {
            try {
                Account account = accountService.findById(accountId);
                if (account == null) {
                    errors.add("La cuenta no existe");
                } else {
                    if (account.isDeleted()) {
                        errors.add("No se puede crear un beneficiario para una cuenta eliminada");
                    }
                    if (!"ACTIVE".equals(account.getStatus())) {
                        errors.add("Solo se pueden agregar beneficiarios para cuentas activas");
                    }
                }
            } catch (EntityNotFoundException e) {
                errors.add("La cuenta no existe");
            }
        }

        // Validar que no exista ya la relación
        if (userId != null && accountId != null && errors.isEmpty()) {
            List<Beneficiary> existing = beneficiaryRepository.findByUserId(userId);
            boolean alreadyExists = existing.stream()
                    .anyMatch(b -> b.getAccount().getId().equals(accountId) && !b.isDeleted());

            if (alreadyExists) {
                errors.add("Ya existe un beneficiario activo para este usuario y cuenta");
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valida si se puede actualizar un beneficiario
     */
    public ValidationResult validateBeneficiaryUpdate(Long beneficiaryId, Long userId, Long accountId) {
        List<String> errors = new ArrayList<>();

        if (beneficiaryId == null) {
            errors.add("El ID del beneficiario es obligatorio");
            return new ValidationResult(false, errors);
        }

        // Verificar que el beneficiario existe
        try {
            Beneficiary existing = beneficiaryRepository.findById(beneficiaryId)
                    .orElseThrow(() -> new EntityNotFoundException("Beneficiario", beneficiaryId));

            if (existing.isDeleted()) {
                errors.add("No se puede actualizar un beneficiario eliminado");
            }
        } catch (EntityNotFoundException e) {
            errors.add("El beneficiario no existe");
            return new ValidationResult(false, errors);
        }

        // Validar los nuevos valores si se proporcionan
        if (userId != null) {
            ValidationResult userValidation = validateUser(userId);
            if (!userValidation.isValid()) {
                errors.addAll(userValidation.getErrors());
            }
        }

        if (accountId != null) {
            ValidationResult accountValidation = validateAccount(accountId);
            if (!accountValidation.isValid()) {
                errors.addAll(accountValidation.getErrors());
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valida si se puede eliminar un beneficiario
     */
    public ValidationResult validateBeneficiaryDeletion(Long beneficiaryId) {
        List<String> errors = new ArrayList<>();

        if (beneficiaryId == null) {
            errors.add("El ID del beneficiario es obligatorio");
            return new ValidationResult(false, errors);
        }

        try {
            Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                    .orElseThrow(() -> new EntityNotFoundException("Beneficiario", beneficiaryId));

            if (beneficiary.isDeleted()) {
                errors.add("El beneficiario ya está eliminado");
            }

            // Aquí se pueden agregar más validaciones de negocio
            // Por ejemplo, verificar si hay transacciones pendientes

        } catch (EntityNotFoundException e) {
            errors.add("El beneficiario no existe");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    private ValidationResult validateUser(Long userId) {
        List<String> errors = new ArrayList<>();

        try {
            User user = userService.findById(userId);
            if (user == null) {
                errors.add("El usuario no existe");
            } else if (user.isDeleted()) {
                errors.add("El usuario está eliminado");
            }
        } catch (EntityNotFoundException e) {
            errors.add("El usuario no existe");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    private ValidationResult validateAccount(Long accountId) {
        List<String> errors = new ArrayList<>();

        try {
            Account account = accountService.findById(accountId);
            if (account == null) {
                errors.add("La cuenta no existe");
            } else {
                if (account.isDeleted()) {
                    errors.add("La cuenta está eliminada");
                }
                if (!"ACTIVE".equals(account.getStatus())) {
                    errors.add("La cuenta no está activa");
                }
            }
        } catch (EntityNotFoundException e) {
            errors.add("La cuenta no existe");
        }

        return new ValidationResult(errors.isEmpty(), errors);
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
