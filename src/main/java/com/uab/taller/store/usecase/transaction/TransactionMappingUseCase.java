package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.request.CreateTransactionRequest;
import com.uab.taller.store.domain.dto.request.UpdateTransactionRequest;
import com.uab.taller.store.domain.dto.response.TransactionDetailResponse;
import com.uab.taller.store.domain.dto.response.TransactionSummaryResponse;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Caso de uso para mapear entidades Transaction hacia y desde DTOs
 */
@Service
public class TransactionMappingUseCase {

    @Autowired
    private IAccountService accountService;

    /**
     * Convierte un CreateTransactionRequest a una entidad Transaction
     */
    public Transaction toEntity(CreateTransactionRequest request) {
        if (request == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionType(request.getTransactionType());
        transaction.setAmount(request.getAmount());
        transaction.setDate(LocalDateTime.now());

        // Establecer cuentas
        if (request.getSourceAccountId() != null) {
            transaction.setSourceAccount(accountService.findById(request.getSourceAccountId()));
        }
        if (request.getTargetAccountId() != null) {
            transaction.setTargetAccount(accountService.findById(request.getTargetAccountId()));
        }

        return transaction;
    }

    /**
     * Actualiza una entidad Transaction existente con los datos de
     * UpdateTransactionRequest
     */
    public Transaction updateEntity(Transaction existing, UpdateTransactionRequest request) {
        if (existing == null || request == null) {
            return existing;
        }

        // Solo se pueden actualizar ciertos campos después de la creación
        // En el contexto bancario, muchos campos son inmutables una vez creada la
        // transacción

        return existing;
    }

    /**
     * Convierte una entidad Transaction a TransactionDetailResponse
     */
    public TransactionDetailResponse toDetailResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        TransactionDetailResponse.TransactionDetailResponseBuilder builder = TransactionDetailResponse.builder()
                .id(transaction.getId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .date(transaction.getDate())
                .description(transaction.getDescription())
                .reference(transaction.getReference())
                .notes(transaction.getNotes())
                .status(transaction.getStatus())
                .currency(transaction.getCurrency());

        // Información de cuenta origen
        if (transaction.getSourceAccount() != null) {
            builder.sourceAccount(TransactionDetailResponse.AccountInfo.builder()
                    .id(transaction.getSourceAccount().getId())
                    .accountNumber(transaction.getSourceAccount().getAccountNumber())
                    .type(transaction.getSourceAccount().getType())
                    .ownerName(transaction.getSourceAccount().getUser() != null
                            ? transaction.getSourceAccount().getUser().getEmail()
                            : "N/A")
                    .build());
        }

        // Información de cuenta destino (si existe)
        if (transaction.getTargetAccount() != null) {
            builder.targetAccount(TransactionDetailResponse.AccountInfo.builder()
                    .id(transaction.getTargetAccount().getId())
                    .accountNumber(transaction.getTargetAccount().getAccountNumber())
                    .type(transaction.getTargetAccount().getType())
                    .ownerName(transaction.getTargetAccount().getUser() != null
                            ? transaction.getTargetAccount().getUser().getEmail()
                            : "N/A")
                    .build());
        }

        return builder.build();
    }

    /**
     * Convierte una entidad Transaction a TransactionSummaryResponse
     */
    public TransactionSummaryResponse toSummaryResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return TransactionSummaryResponse.builder()
                .id(transaction.getId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .date(transaction.getDate())
                .description(transaction.getDescription())
                .sourceAccountNumber(
                        transaction.getSourceAccount() != null ? transaction.getSourceAccount().getAccountNumber()
                                : null)
                .targetAccountNumber(
                        transaction.getTargetAccount() != null ? transaction.getTargetAccount().getAccountNumber()
                                : null)
                .status(transaction.getStatus())
                .currency(transaction.getCurrency())
                .build();
    }
}
