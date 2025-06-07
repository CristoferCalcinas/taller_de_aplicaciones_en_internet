package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetAccountByNumberUseCase {

    @Autowired
    private IAccountService accountService;

    public Account getByAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de cuenta no puede estar vacío");
        }

        Optional<Account> account = accountService.findByAccountNumber(accountNumber.trim());

        return account.orElseThrow(
                () -> new EntityNotFoundException("Cuenta con número " + accountNumber + " no encontrada"));
    }

    public boolean existsByAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }

        return accountService.findByAccountNumber(accountNumber.trim()).isPresent();
    }
}
