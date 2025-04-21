package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.AccountPostRequest;
import com.uab.taller.store.service.IAccountService;
import com.uab.taller.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UpdateAccountUseCase {
    @Autowired
    IAccountService accountService;

    @Autowired
    IUserService userService;

    public Account updateAccount(AccountPostRequest accountPostRequest) {
        if (accountPostRequest.getId() == null) {
            throw new RuntimeException("No existe la cuenta a actualizar");
        }

        Account account = accountService.getById(accountPostRequest.getId());
        if (account == null) {
            throw new RuntimeException("No existe la cuenta a actualizar");
        }

        if (accountPostRequest.getSaldo() >= 0.0) {
            account.setSaldo(accountPostRequest.getSaldo());
        }

        if (accountPostRequest.getType() != null && !accountPostRequest.getType().isEmpty()) {
            account.setType(accountPostRequest.getType());
        }

        if (accountPostRequest.getUserId() != null) {
            User user = userService.getById(accountPostRequest.getUserId());
            if (user == null) {
                throw new RuntimeException("Usuario no encontrado con ID: " + accountPostRequest.getUserId());
            }

            account.setUser(user);
        }

        return accountService.update(account);
    }
}
