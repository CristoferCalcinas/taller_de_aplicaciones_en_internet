package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.service.interfaces.IBeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteBeneficiaryUseCase {
    @Autowired
    IBeneficiaryService service;

    public void deleteById(Long id) {
        service.deleteById(id);
    }
}
