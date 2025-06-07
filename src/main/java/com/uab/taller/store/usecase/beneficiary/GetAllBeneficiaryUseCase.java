package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.service.interfaces.IBeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllBeneficiaryUseCase {
    @Autowired
    IBeneficiaryService service;

    public List<Beneficiary> getAll() {
        return service.findAll();
    }
}
