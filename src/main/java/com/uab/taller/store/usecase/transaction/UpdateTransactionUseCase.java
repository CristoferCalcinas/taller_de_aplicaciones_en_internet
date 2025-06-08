package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.request.UpdateTransactionRequest;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para actualizar transacciones con validaciones de negocio
 */
@Service
public class UpdateTransactionUseCase {

    @Autowired
    private ITransactionService transactionService;

    @Transactional
    public Transaction update(Transaction transaction) {
        return transactionService.update(transaction);
    }

    /**
     * Construye una transacción a partir de un UpdateTransactionRequest
     */
    @Transactional
    public Transaction buildTransactionFromUpdateRequest(Long id, UpdateTransactionRequest request) {
        // Obtener la transacción existente
        Transaction existingTransaction = transactionService.findById(id);
        if (existingTransaction == null) {
            throw new EntityNotFoundException("Transacción", id);
        }

        // Validar que solo se pueden actualizar ciertos campos
        if (request.getDescription() != null) {
            existingTransaction.setDescription(request.getDescription());
        }
        if (request.getNotes() != null) {
            existingTransaction.setNotes(request.getNotes());
        }
        if (request.getReference() != null) {
            existingTransaction.setReference(request.getReference());
        }
        // Los campos críticos como amount, sourceAccount, targetAccount no se pueden
        // modificar

        return existingTransaction;
    }
}
