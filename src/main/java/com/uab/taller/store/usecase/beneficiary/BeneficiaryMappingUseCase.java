package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.domain.dto.response.BeneficiaryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeneficiaryMappingUseCase {

    public BeneficiaryResponse toResponse(Beneficiary beneficiary) {
        if (beneficiary == null) {
            return null;
        }

        BeneficiaryResponse response = new BeneficiaryResponse();
        response.setId(beneficiary.getId());
        response.setAlias(beneficiary.getAlias());
        response.setDescription(beneficiary.getDescription());
        response.setDeleted(beneficiary.isDeleted());
        response.setAddDate(beneficiary.getAddDate());
        response.setChangeDate(beneficiary.getChangeDate());
        response.setAddUser(beneficiary.getAddUser());
        response.setChangeUser(beneficiary.getChangeUser()); // Mapear información del usuario
        if (beneficiary.getUser() != null) {
            response.setUserId(beneficiary.getUser().getId());
            response.setUserEmail(beneficiary.getUser().getEmail());
        }

        // Mapear información de la cuenta
        if (beneficiary.getAccount() != null) {
            response.setAccountId(beneficiary.getAccount().getId());
            response.setAccountNumber(beneficiary.getAccount().getAccountNumber());
            response.setAccountType(beneficiary.getAccount().getType());
            response.setAccountStatus(beneficiary.getAccount().getStatus());
            response.setAccountCurrency(beneficiary.getAccount().getCurrency());
        }

        return response;
    }

    /**
     * Convierte una lista de entidades Beneficiary a lista de BeneficiaryResponse
     */
    public List<BeneficiaryResponse> toResponseList(List<Beneficiary> beneficiaries) {
        if (beneficiaries == null) {
            return null;
        }

        return beneficiaries.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
