package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.dto.request.AccountRequest;
import com.uab.taller.store.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAccountUseCase {
    @Autowired
    IAccountService accountService;

    public Account save(AccountRequest accountRequest){
        Account account = new Account();
        account.setType(accountRequest.getType());
        account.setSaldo(accountRequest.getSaldo());

//        int lastId = accountService.getLastCreatedCardNumber();
//
//        account.setNumber(lastId + 1);

        return accountService.save(account);
    }

}
