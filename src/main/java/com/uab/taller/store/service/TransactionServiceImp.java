package com.uab.taller.store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.repository.TransactionRepository;
import com.uab.taller.store.service.interfaces.ITransactionService;

@Service
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
                .orElseThrow(() -> new EntityNotFoundException("Transacción", id));
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Transacción", id);
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new EntityDeletionException("Transacción", id, "Error durante la eliminación: " + e.getMessage());
        }
    }

    @Override
    public Transaction update(Transaction entity) {
        if (repository.existsById(entity.getId())) {
            return repository.save(entity);
        } else {
            throw new EntityNotFoundException("Transacción", entity.getId());
        }
    }
}
