package com.uab.taller.store.usecase.transaction;

import com.uab.taller.store.domain.Transaction;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class DeleteTransactionUseCase {
    @Autowired
    private ITransactionService transactionService;

    @Transactional
    public DeleteResponse deleteById(Long id) {
        // Validar que el ID no sea nulo
        if (id == null) {
            throw new IllegalArgumentException("El ID de la transacción no puede ser nulo");
        }

        // Verificar que la transacción existe
        Transaction transaction = transactionService.findById(id);
        if (transaction == null) {
            throw new EntityNotFoundException("Transacción", id);
        }

        // Validar que la transacción se puede eliminar (ej: no más de 24 horas)
        LocalDateTime now = LocalDateTime.now();
        long hoursElapsed = ChronoUnit.HOURS.between(transaction.getDate(), now);

        if (hoursElapsed > 24) {
            throw new EntityDeletionException("Transacción", id,
                    "No se pueden eliminar transacciones con más de 24 horas de antigüedad");
        }

        // Validar el tipo de transacción - algunas pueden ser no reversibles
        if ("DEPOSIT".equals(transaction.getTransactionType()) && hoursElapsed > 1) {
            throw new EntityDeletionException("Transacción", id,
                    "Los depósitos no se pueden eliminar después de 1 hora");
        }

        try {
            // Para transacciones, generalmente se prefiere crear una transacción reversa
            // en lugar de eliminar la original, pero aquí implementamos la eliminación
            transactionService.deleteById(id);

            return DeleteResponse.success(id, "Transacción");
        } catch (Exception e) {
            throw new EntityDeletionException("Transacción", id,
                    "Error interno durante la eliminación: " + e.getMessage());
        }
    }

    // Método para crear transacción reversa (recomendado para transacciones
    // financieras)
    @Transactional
    public DeleteResponse reverseTransaction(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la transacción no puede ser nulo");
        }

        Transaction originalTransaction = transactionService.findById(id);
        if (originalTransaction == null) {
            throw new EntityNotFoundException("Transacción", id);
        }

        try {
            // Crear transacción reversa
            Transaction reverseTransaction = new Transaction();
            reverseTransaction.setSourceAccount(originalTransaction.getTargetAccount());
            reverseTransaction.setTargetAccount(originalTransaction.getSourceAccount());
            reverseTransaction.setAmount(originalTransaction.getAmount());
            reverseTransaction.setTransactionType("REVERSE_" + originalTransaction.getTransactionType());
            reverseTransaction.setDate(LocalDateTime.now());

            transactionService.save(reverseTransaction);

            return new DeleteResponse(true,
                    String.format("Transacción %d reversada exitosamente con ID %d",
                            id, reverseTransaction.getId()),
                    reverseTransaction.getId(),
                    "Transacción Reversa");
        } catch (Exception e) {
            throw new EntityDeletionException("Transacción", id,
                    "Error durante la creación de transacción reversa: " + e.getMessage());
        }
    }
}
