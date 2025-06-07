package com.uab.taller.store.service;

import java.util.List;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.repository.TransactionRepository;
import com.uab.taller.store.service.interfaces.ITransactionService;

public class TransactionServiceImp implements ITransactionService {

    TransactionRepository repository;

    public TransactionServiceImp(TransactionRepository transactionRepository) {
        this.repository = transactionRepository;
    }

    @Override
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    @Override
    public Transaction save(Transaction entity) {
        if (entity.getId() == null) {
            return repository.save(entity);
        } else {
            return update(entity);
        }
    }

    @Override
    public Transaction findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
    }

    @Override
    public Transaction update(Transaction entity) {
        if (repository.existsById(entity.getId())) {
            return repository.save(entity);
        } else {
            throw new RuntimeException("Transaction not found with id: " + entity.getId());
        }
    }
}
