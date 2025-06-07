package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllAccountUseCase {
    @Autowired
    IAccountService accountService;

    public List<Account> getAll() {
        return accountService.findAll();
    }
}
