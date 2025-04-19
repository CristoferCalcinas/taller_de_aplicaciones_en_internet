package com.uab.taller.store.service;

import com.uab.taller.store.domain.Account;

import java.util.List;

public interface IAccountService {
    Account save(Account account);
    List<Account> getAll();
    Account getById(Long id);
    void deleteById(Long id);
    Account update(Account account);
}
