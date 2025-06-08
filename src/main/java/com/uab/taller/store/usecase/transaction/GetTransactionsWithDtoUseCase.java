package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.response.TransactionDetailResponse;
import com.uab.taller.store.domain.dto.response.TransactionSummaryResponse;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso mejorado para obtener transacciones con DTOs
 */
@Service
public class GetTransactionsWithDtoUseCase {

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private TransactionMappingUseCase mappingUseCase;

    /**
     * Obtiene todas las transacciones como resumen
     */
    public List<TransactionSummaryResponse> getAllTransactionsSummary() {
        List<Transaction> transactions = transactionService.findAll();
        return transactions.stream()
                .map(mappingUseCase::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una transacción por ID con detalles completos
     */
    public TransactionDetailResponse getTransactionById(Long id) {
        Transaction transaction = transactionService.findById(id);
        return mappingUseCase.toDetailResponse(transaction);
    }

    /**
     * Obtiene transacciones de una cuenta específica
     */
    public List<TransactionSummaryResponse> getTransactionsByAccount(Long accountId) {
        // Obtener transacciones de la cuenta usando el repositorio
        List<Transaction> transactions = transactionService.findAll().stream()
                .filter(t -> (t.getSourceAccount() != null && t.getSourceAccount().getId().equals(accountId)) ||
                        (t.getTargetAccount() != null && t.getTargetAccount().getId().equals(accountId)))
                .collect(Collectors.toList());

        return transactions.stream()
                .map(mappingUseCase::toSummaryResponse)
                .collect(Collectors.toList());
    }
}
