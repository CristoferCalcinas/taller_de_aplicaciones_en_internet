package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.dto.request.UpdateAccountRequest;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateAccountUseCase {

    @Autowired
    private IAccountService accountService;

    @Transactional
    public Account updateAccount(UpdateAccountRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("El ID de la cuenta es obligatorio");
        }

        Account account = accountService.findById(request.getId());
        if (account == null) {
            throw new EntityNotFoundException("Cuenta", request.getId());
        }

        // Validar que la cuenta no esté cerrada
        if ("CLOSED".equals(account.getStatus())) {
            throw new IllegalStateException("No se puede actualizar una cuenta cerrada");
        }

        // Actualizar campos permitidos
        if (request.getType() != null && !request.getType().trim().isEmpty()) {
            account.setType(request.getType());
        }

        if (request.getCurrency() != null && !request.getCurrency().trim().isEmpty()) {
            account.setCurrency(request.getCurrency());
        }

        if (request.getBalance() != null) {
            account.setBalance(request.getBalance());
        }

        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            // Validar transiciones de estado válidas
            validateStatusTransition(account.getStatus(), request.getStatus());
            account.setStatus(request.getStatus());
        }

        return accountService.update(account);
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        // ACTIVE -> SUSPENDED, CLOSED
        // SUSPENDED -> ACTIVE, CLOSED
        // CLOSED -> no se puede cambiar

        if ("CLOSED".equals(currentStatus)) {
            throw new IllegalStateException("No se puede cambiar el estado de una cuenta cerrada");
        }

        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException("Estado no válido: " + newStatus);
        }
    }

    private boolean isValidStatus(String status) {
        return "ACTIVE".equals(status) || "SUSPENDED".equals(status) || "CLOSED".equals(status);
    }
}
