package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.request.CreateTransactionRequest;
import com.uab.taller.store.domain.dto.response.TransactionDetailResponse;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso mejorado para crear transacciones usando DTOs
 */
@Service
public class CreateTransactionWithDtoUseCase {

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private TransactionMappingUseCase mappingUseCase;

    @Transactional
    public TransactionDetailResponse createTransaction(CreateTransactionRequest request) {
        // Convertir DTO a entidad
        Transaction transaction = mappingUseCase.toEntity(request);

        // Guardar la transacción
        Transaction savedTransaction = transactionService.save(transaction);

        // Convertir entidad a DTO de respuesta
        return mappingUseCase.toDetailResponse(savedTransaction);
    }
}
