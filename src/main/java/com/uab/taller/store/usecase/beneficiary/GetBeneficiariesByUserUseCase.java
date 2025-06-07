package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetBeneficiariesByUserUseCase {
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    public List<Beneficiary> getBeneficiariesByUser(Long userId) {
        return beneficiaryRepository.findByUserId(userId);
    }

    public List<Beneficiary> getActiveBeneficiariesByUser(Long userId) {
        return beneficiaryRepository.findByUserIdAndActiveTrue(userId);
    }
}
