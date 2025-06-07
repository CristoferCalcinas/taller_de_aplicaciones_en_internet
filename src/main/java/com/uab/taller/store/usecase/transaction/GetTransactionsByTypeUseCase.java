package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTransactionsByTypeUseCase {
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByType(String transactionType) {
        return transactionRepository.findByTransactionTypeIgnoreCase(transactionType);
    }
}
