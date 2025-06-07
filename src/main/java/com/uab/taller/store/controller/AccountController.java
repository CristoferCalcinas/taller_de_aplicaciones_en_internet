package com.uab.taller.store.controller;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.dto.request.AccountPostRequest;
import com.uab.taller.store.domain.dto.request.AccountRequest;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.usecase.account.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    CreateAccountUseCase createAccountUseCase;

    @Autowired
    GetAllAccountUseCase getAllAccountUseCase;

    @Autowired
    GetAccountByIdUseCase getAccountByIdUseCase;

    @Autowired
    DeleteAccountUseCase deleteAccountUseCase;

    @Autowired
    UpdateAccountUseCase updateAccountUseCase;

    @PostMapping
    public Account createAccount(@RequestBody AccountRequest accountRequest) {
        return createAccountUseCase.save(accountRequest);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return getAllAccountUseCase.getAll();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return getAccountByIdUseCase.getById(id);
    }

    @DeleteMapping("/{id}")
    public DeleteResponse deleteAccountById(@PathVariable Long id) {
        return deleteAccountUseCase.deleteById(id);
    }

    @DeleteMapping("/{id}/force")
    public DeleteResponse forceDeleteAccountById(@PathVariable Long id) {
        return deleteAccountUseCase.forceDeleteById(id);
    }

    @PutMapping
    public Account updateAccountById(@RequestBody AccountPostRequest account) {
        return updateAccountUseCase.updateAccount(account);
    }
}
