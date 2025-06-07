package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.service.interfaces.IBeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetBeneficiaryByIdUseCase {
    @Autowired
    IBeneficiaryService service;

    public Beneficiary getById(Long id) {
        return service.findById(id);
    }
}
