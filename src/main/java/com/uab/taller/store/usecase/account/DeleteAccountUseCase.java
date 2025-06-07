package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.usecase.validation.DeletionValidationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteAccountUseCase {
    @Autowired
    IAccountService accountService;

    @Autowired
    private DeletionValidationUseCase deletionValidationUseCase;

    @Transactional
    public DeleteResponse deleteById(Long id) {
        // Validar que el ID no sea nulo
        if (id == null) {
            throw new IllegalArgumentException("El ID de la cuenta no puede ser nulo");
        } // Verificar que la cuenta existe
        Account account = accountService.findById(id);
        if (account == null) {
            throw new EntityNotFoundException("Cuenta", id);
        }

        // Usar el validador centralizado
        deletionValidationUseCase.validateAccountDeletion(account);

        try {
            // Realizar soft delete
            account.setStatus("CLOSED");
            account.setDeleted(true);
            account.setChangeUser("SYSTEM_DELETE");
            accountService.update(account);

            return DeleteResponse.success(id, "Cuenta");
        } catch (Exception e) {
            throw new EntityDeletionException("Cuenta", id,
                    "Error interno durante la eliminación: " + e.getMessage());
        }
    }

    // Método para eliminación física (solo para casos especiales)
    @Transactional
    public DeleteResponse forceDeleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la cuenta no puede ser nulo");
        }

        Account account = accountService.findById(id);
        if (account == null) {
            throw new EntityNotFoundException("Cuenta", id);
        }

        try {
            accountService.deleteById(id);
            return DeleteResponse.success(id, "Cuenta");
        } catch (Exception e) {
            throw new EntityDeletionException("Cuenta", id,
                    "Error durante la eliminación física: " + e.getMessage());
        }
    }
}
