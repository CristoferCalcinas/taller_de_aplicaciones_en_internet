package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.response.DeleteResponse;
import com.uab.taller.store.exception.EntityDeletionException;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IUserService;
import com.uab.taller.store.usecase.audit.DeletionAuditUseCase;
import com.uab.taller.store.usecase.validation.DeletionValidationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteUserUseCase {
    @Autowired
    private IUserService userService;

    @Autowired
    private DeletionValidationUseCase deletionValidationUseCase;

    @Autowired
    private DeletionAuditUseCase deletionAuditUseCase;

    @Transactional
    public DeleteResponse deleteUserById(Long id) {
        // Validar que el ID no sea nulo
        if (id == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        // Verificar que el usuario exists
        User user = userService.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("Usuario", id);
        } // Usar el validador centralizado
        try {
            deletionValidationUseCase.validateUserDeletion(user);
        } catch (EntityDeletionException e) {
            deletionAuditUseCase.logDeletionAttempt("Usuario", id, e.getMessage(), "SYSTEM");
            throw e;
        }

        try {
            // Realizar soft delete marcando como eliminado
            user.setDeleted(true);
            user.setChangeUser("SYSTEM_DELETE");
            userService.update(user);

            // Registrar la eliminación en auditoría
            deletionAuditUseCase.logDeletion("Usuario", id, "SOFT_DELETE", "SYSTEM");

            return DeleteResponse.success(id, "Usuario");
        } catch (Exception e) {
            throw new EntityDeletionException("Usuario", id,
                    "Error interno durante la eliminación: " + e.getMessage());
        }
    }

    // Método para eliminación física (solo para casos especiales)
    @Transactional
    public DeleteResponse forceDeleteUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        User user = userService.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("Usuario", id);
        }
        try {
            userService.deleteById(id);

            // Registrar la eliminación física en auditoría
            deletionAuditUseCase.logForceDelete("Usuario", id, "SYSTEM");

            return DeleteResponse.success(id, "Usuario");
        } catch (Exception e) {
            throw new EntityDeletionException("Usuario", id,
                    "Error durante la eliminación física: " + e.getMessage());
        }
    }
}
