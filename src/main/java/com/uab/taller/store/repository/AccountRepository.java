package com.uab.taller.store.repository;

import com.uab.taller.store.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
