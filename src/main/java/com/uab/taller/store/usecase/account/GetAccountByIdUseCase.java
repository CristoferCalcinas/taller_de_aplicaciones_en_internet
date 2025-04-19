package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAccountByIdUseCase {
    @Autowired
    IAccountService accountService;

    public Account getById(Long id) {
        return accountService.getById(id);
    }
}
