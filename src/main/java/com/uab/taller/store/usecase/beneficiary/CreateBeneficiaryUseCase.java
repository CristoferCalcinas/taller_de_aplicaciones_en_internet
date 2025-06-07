package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.service.interfaces.IBeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateBeneficiaryUseCase {
    @Autowired
    IBeneficiaryService service;

    public Beneficiary save(Beneficiary beneficiary) {
        // Profile profile = new Profile();
        // profile.setName(profileRequest.getName());
        // profile.setLastName(profileRequest.getLastName());
        // profile.setGender(profileRequest.getGender());
        // profile.setBirthDate(profileRequest.getBirthDate());
        return service.save(beneficiary);
    }
}
