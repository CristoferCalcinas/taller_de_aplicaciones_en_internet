package com.uab.taller.store.controller;

import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.domain.dto.request.BeneficiaryRequest;
import com.uab.taller.store.usecase.beneficiary.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/beneficiary")
public class BeneficiaryController {
    @Autowired
    CreateBeneficiaryUseCase createBeneficiaryUseCase;

    @Autowired
    GetAllBeneficiaryUseCase getAllBeneficiaryUseCase;

    @Autowired
    GetBeneficiaryByIdUseCase getBeneficiaryByIdUseCase;

    @Autowired
    DeleteBeneficiaryUseCase deleteBeneficiaryUseCase;

    @Autowired
    UpdateBeneficiaryUseCase updateBeneficiaryUseCase;

    @Autowired
    GetBeneficiariesByUserUseCase getBeneficiariesByUserUseCase;

    @PostMapping
    public Beneficiary createBeneficiary(@RequestBody BeneficiaryRequest beneficiaryRequest) {
        return createBeneficiaryUseCase.save(beneficiaryRequest);
    }

    @GetMapping
    public List<Beneficiary> getAllBeneficiaries() {
        return getAllBeneficiaryUseCase.getAll();
    }

    @GetMapping("/{id}")
    public Beneficiary getBeneficiaryById(@PathVariable Long id) {
        return getBeneficiaryByIdUseCase.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBeneficiaryById(@PathVariable Long id) {
        deleteBeneficiaryUseCase.deleteById(id);
    }

    @PutMapping
    public Beneficiary updateBeneficiary(@RequestBody Beneficiary beneficiary) {
        return updateBeneficiaryUseCase.update(beneficiary);
    }

    @GetMapping("/user/{userId}")
    public List<Beneficiary> getBeneficiariesByUser(@PathVariable Long userId) {
        return getBeneficiariesByUserUseCase.getBeneficiariesByUser(userId);
    }

    @GetMapping("/user/{userId}/active")
    public List<Beneficiary> getActiveBeneficiariesByUser(@PathVariable Long userId) {
        return getBeneficiariesByUserUseCase.getActiveBeneficiariesByUser(userId);
    }
}
