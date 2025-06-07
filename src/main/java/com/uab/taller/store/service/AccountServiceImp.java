package com.uab.taller.store.service;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.repository.AccountRepository;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImp implements IAccountService {
    AccountRepository accountRepository;

    public AccountServiceImp(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account save(Account account) {
        int lastId = getLastCreatedCardNumber();
        // account.setNumber(lastId + 1);
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta", id));
    }

    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void deleteById(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new EntityNotFoundException("Cuenta", id);
        }
        try {
            accountRepository.deleteById(id);
        } catch (Exception e) {
            throw new EntityDeletionException("Cuenta", id, "Error durante la eliminación: " + e.getMessage());
        }
    }

    public int getLastCreatedCardNumber() {
        List<Account> allAccounts = accountRepository.findAll();
        if (!allAccounts.isEmpty()) {
            Account lastAccount = allAccounts.get(allAccounts.size() - 1);
            // System.out.println(lastAccount.getId());
            return 1000010000;

            // int lastCardNumber = lastAccount.getNumber();
            // if (lastCardNumber == 0){
            // return 1000010000;
            // }
            // return lastAccount.getNumber() ;
        } else {
            return 1000010000;
        }
    }
}
