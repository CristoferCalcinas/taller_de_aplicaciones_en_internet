package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.dto.request.AccountPostRequest;
import com.uab.taller.store.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateAccountUseCase {
    @Autowired
    IAccountService accountService;

    public Account updateAccount(AccountPostRequest accountPostRequest) {
        Account account = accountService.getById(accountPostRequest.getId());
        if(account == null){
            throw new RuntimeException("No existe la cuenta a actualizar");
        }

        if(accountPostRequest.getId()==null){
            throw new RuntimeException("No existe la cuenta a actualizar");
        }

        if(accountPostRequest.getSaldo() >= 0.0){
            account.setSaldo(accountPostRequest.getSaldo());
        }

        if(accountPostRequest.getType() != null && !accountPostRequest.getType().isEmpty()){
            account.setType(accountPostRequest.getType());
        }
        return accountService.update(account);
    }

}
