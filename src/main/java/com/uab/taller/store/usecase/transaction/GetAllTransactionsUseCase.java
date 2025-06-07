package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTransactionsUseCase {
    @Autowired
    private ITransactionService transactionService;

    public List<Transaction> getAllTransactions() {
        return transactionService.findAll();
    }
}
