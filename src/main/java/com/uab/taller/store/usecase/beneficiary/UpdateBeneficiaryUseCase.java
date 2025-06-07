package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.service.interfaces.IBeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateBeneficiaryUseCase {
    @Autowired
    IBeneficiaryService service;

    public Beneficiary update(Beneficiary beneficiary) {
        return service.update(beneficiary);
    }

}
