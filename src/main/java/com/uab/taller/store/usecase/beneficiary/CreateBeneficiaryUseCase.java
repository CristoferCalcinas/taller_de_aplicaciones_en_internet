package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.BeneficiaryRequest;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.service.interfaces.IBeneficiaryService;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateBeneficiaryUseCase {

    @Autowired
    private IBeneficiaryService beneficiaryService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private BeneficiaryValidationUseCase validationUseCase;

    @Transactional
    public Beneficiary save(BeneficiaryRequest beneficiaryRequest) {
        // Validar la creación del beneficiario
        BeneficiaryValidationUseCase.ValidationResult validation = validationUseCase.validateBeneficiaryCreation(
                beneficiaryRequest.getUserId(),
                beneficiaryRequest.getAccountId());

        if (!validation.isValid()) {
            throw new IllegalArgumentException("Error de validación: " + validation.getErrorMessage());
        }

        // Obtener entidades relacionadas
        User user = userService.findById(beneficiaryRequest.getUserId());
        Account account = accountService.findById(beneficiaryRequest.getAccountId());

        // Crear el beneficiario
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setUser(user);
        beneficiary.setAccount(account);
        beneficiary.setAlias(beneficiaryRequest.getAlias());
        beneficiary.setDescription(beneficiaryRequest.getDescription());
        beneficiary.setChangeUser("SYSTEM_CREATE");

        return beneficiaryService.save(beneficiary);
    }
}
