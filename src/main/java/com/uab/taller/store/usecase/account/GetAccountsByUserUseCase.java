package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.dto.response.AccountResponse;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAccountsByUserUseCase {

    @Autowired
    private IAccountService accountService;

    public List<AccountResponse> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountService.findAccountsByUserId(userId);
        return accounts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<AccountResponse> getActiveAccountsByUserId(Long userId) {
        List<Account> accounts = accountService.findActiveAccountsByUserId(userId);
        return accounts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private AccountResponse convertToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .currency(account.getCurrency())
                .type(account.getType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .userId(account.getUser().getId())
                .userEmail(account.getUser().getEmail())
                .userName(account.getUser().getProfile() != null ? account.getUser().getProfile().getName() : null)
                .userLastName(
                        account.getUser().getProfile() != null ? account.getUser().getProfile().getLastName() : null)
                .addDate(account.getAddDate())
                .changeDate(account.getChangeDate())
                .addUser(account.getAddUser())
                .changeUser(account.getChangeUser())
                .totalTransactions(
                        (account.getOutgoingTransactions() != null ? account.getOutgoingTransactions().size() : 0) +
                                (account.getIncomingTransactions() != null ? account.getIncomingTransactions().size()
                                        : 0))
                .activeBeneficiaries(
                        account.getAccountBeneficiaries() != null ? (int) account.getAccountBeneficiaries().stream()
                                .filter(b -> !b.isDeleted())
                                .count() : 0)
                .build();
    }
}
