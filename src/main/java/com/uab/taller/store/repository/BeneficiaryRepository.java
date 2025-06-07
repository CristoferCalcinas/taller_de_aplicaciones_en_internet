package com.uab.taller.store.repository;

import com.uab.taller.store.domain.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByUserId(Long userId);

    List<Beneficiary> findByUserIdAndDeletedFalse(Long userId);
}
