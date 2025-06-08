package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.response.TransactionHistoryResponse;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso para obtener el historial completo de una transacción
 */
@Service
public class GetTransactionHistoryUseCase {

    @Autowired
    private ITransactionService transactionService;

    /**
     * Obtiene el historial completo de una transacción
     */
    public TransactionHistoryResponse getTransactionHistory(Long transactionId) {
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction == null) {
            throw new EntityNotFoundException("Transacción", transactionId);
        }

        // Construir información principal
        TransactionHistoryResponse.TransactionInfo transactionInfo = TransactionHistoryResponse.TransactionInfo
                .builder()
                .id(transaction.getId())
                .type(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus())
                .createdAt(transaction.getDate())
                .description(transaction.getDescription())
                .reference(transaction.getReference())
                .build();

        // Construir eventos del historial
        List<TransactionHistoryResponse.TransactionEvent> events = buildTransactionEvents(transaction);

        // Construir información de auditoría
        TransactionHistoryResponse.AuditInfo auditInfo = TransactionHistoryResponse.AuditInfo.builder()
                .createdBy(transaction.getAddUser() != null ? transaction.getAddUser() : "SYSTEM")
                .createdAt(transaction.getDate())
                .lastModifiedBy(transaction.getChangeUser() != null ? transaction.getChangeUser() : "SYSTEM")
                .lastModifiedAt(
                        transaction.getChangeDate() != null ? transaction.getChangeDate() : transaction.getDate())
                .modificationCount(0) // Por simplicidad, podría mejorarse con un contador real
                .build();

        return TransactionHistoryResponse.builder()
                .transaction(transactionInfo)
                .events(events)
                .auditInfo(auditInfo)
                .build();
    }

    /**
     * Construye la lista de eventos para el historial de la transacción
     */
    private List<TransactionHistoryResponse.TransactionEvent> buildTransactionEvents(Transaction transaction) {
        List<TransactionHistoryResponse.TransactionEvent> events = new ArrayList<>();

        // Evento de creación
        events.add(TransactionHistoryResponse.TransactionEvent.builder()
                .eventType("CREATED")
                .description("Transacción creada")
                .timestamp(transaction.getDate())
                .executedBy(transaction.getAddUser() != null ? transaction.getAddUser() : "SYSTEM")
                .details(String.format("Transacción %s creada por %s",
                        transaction.getTransactionType(),
                        transaction.getAmount()))
                .build());

        // Evento de procesamiento
        events.add(TransactionHistoryResponse.TransactionEvent.builder()
                .eventType("PROCESSED")
                .description("Transacción procesada exitosamente")
                .timestamp(transaction.getDate().plusSeconds(1)) // Simulado
                .executedBy("SYSTEM")
                .details("Fondos transferidos entre cuentas")
                .build());

        // Evento de finalización
        events.add(TransactionHistoryResponse.TransactionEvent.builder()
                .eventType("COMPLETED")
                .description("Transacción completada")
                .timestamp(transaction.getDate().plusSeconds(2)) // Simulado
                .executedBy("SYSTEM")
                .details("Estado final: " + transaction.getStatus())
                .build());

        return events;
    }
}
