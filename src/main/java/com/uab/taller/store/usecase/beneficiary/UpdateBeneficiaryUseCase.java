package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.UpdateBeneficiaryRequest;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.service.interfaces.IBeneficiaryService;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateBeneficiaryUseCase {

    @Autowired
    private IBeneficiaryService beneficiaryService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private BeneficiaryValidationUseCase validationUseCase;

    @Transactional
    public Beneficiary update(UpdateBeneficiaryRequest request) {
        // Validar la actualización del beneficiario
        BeneficiaryValidationUseCase.ValidationResult validation = validationUseCase.validateBeneficiaryUpdate(
                request.getId(),
                request.getUserId(),
                request.getAccountId());

        if (!validation.isValid()) {
            throw new IllegalArgumentException("Error de validación: " + validation.getErrorMessage());
        }

        // Obtener el beneficiario existente
        Beneficiary beneficiary = beneficiaryService.findById(request.getId());
        if (beneficiary == null) {
            throw new EntityNotFoundException("Beneficiario", request.getId());
        }

        // Actualizar campos si se proporcionan nuevos valores
        if (request.getUserId() != null) {
            User user = userService.findById(request.getUserId());
            beneficiary.setUser(user);
        }

        if (request.getAccountId() != null) {
            Account account = accountService.findById(request.getAccountId());
            beneficiary.setAccount(account);
        }

        // Actualizar alias y descripción (pueden ser null para limpiar)
        beneficiary.setAlias(request.getAlias());
        beneficiary.setDescription(request.getDescription());
        beneficiary.setChangeUser("SYSTEM_UPDATE");

        return beneficiaryService.update(beneficiary);
    }

    // Método legacy para mantener compatibilidad
    public Beneficiary update(Beneficiary beneficiary) {
        if (beneficiary == null || beneficiary.getId() == null) {
            throw new IllegalArgumentException("El beneficiario y su ID son obligatorios");
        }

        // Validar que el beneficiario existe
        Beneficiary existing = beneficiaryService.findById(beneficiary.getId());
        if (existing == null) {
            throw new EntityNotFoundException("Beneficiario", beneficiary.getId());
        }

        // Validar usando el sistema de validación si hay cambios en user/account
        if (beneficiary.getUser() != null || beneficiary.getAccount() != null) {
            Long userId = beneficiary.getUser() != null ? beneficiary.getUser().getId() : null;
            Long accountId = beneficiary.getAccount() != null ? beneficiary.getAccount().getId() : null;

            BeneficiaryValidationUseCase.ValidationResult validation = validationUseCase
                    .validateBeneficiaryUpdate(beneficiary.getId(), userId, accountId);

            if (!validation.isValid()) {
                throw new IllegalArgumentException("Error de validación: " + validation.getErrorMessage());
            }
        }

        beneficiary.setChangeUser("SYSTEM_UPDATE");
        return beneficiaryService.update(beneficiary);
    }
}
