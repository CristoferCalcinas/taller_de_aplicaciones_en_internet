package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteTransactionUseCase {
    @Autowired
    private ITransactionService transactionService;

    public void deleteById(Long id) {
        transactionService.deleteById(id);
    }
}
