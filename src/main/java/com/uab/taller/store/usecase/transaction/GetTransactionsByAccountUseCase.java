package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTransactionsByAccountUseCase {
    @Autowired
    private ITransactionService transactionService;

    public List<Transaction> getTransactionsByAccount(Long accountId) {
        return transactionService.findAll().stream()
                .filter(transaction -> 
                    (transaction.getSourceAccount() != null && transaction.getSourceAccount().getId().equals(accountId)) ||
                    (transaction.getTargetAccount() != null && transaction.getTargetAccount().getId().equals(accountId))
                )
                .toList();
    }
}
