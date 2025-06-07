package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTransactionByIdUseCase {
    @Autowired
    private ITransactionService transactionService;

    public Transaction getById(Long id) {
        return transactionService.findById(id);
    }
}
