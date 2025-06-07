package com.uab.taller.store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.repository.BeneficiaryRepository;
import com.uab.taller.store.service.interfaces.IBeneficiaryService;

@Service
public class BeneficiaryServiceImp implements IBeneficiaryService {

    BeneficiaryRepository repository;

    public BeneficiaryServiceImp(BeneficiaryRepository beneficiaryRepository) {
        this.repository = beneficiaryRepository;
    }

    @Override
    public List<Beneficiary> findAll() {
        return repository.findAll();
    }

    @Override
    public Beneficiary save(Beneficiary entity) {
        return repository.save(entity);
    }

    @Override
    public Beneficiary findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Beneficiario", id));
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Beneficiario", id);
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new EntityDeletionException("Beneficiario", id, "Error durante la eliminación: " + e.getMessage());
        }
    }

    @Override
    public Beneficiary update(Beneficiary entity) {
        if (repository.existsById(entity.getId())) {
            return repository.save(entity);
        } else {
            throw new EntityNotFoundException("Beneficiario", entity.getId());
        }
    }
}
