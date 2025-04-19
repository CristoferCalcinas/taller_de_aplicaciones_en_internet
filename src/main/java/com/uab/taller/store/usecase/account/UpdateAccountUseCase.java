package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateAccountUseCase {
    @Autowired
    IAccountService accountService;

    public Account updateAccount(Account account) {
        return accountService.update(account);
    }

}
